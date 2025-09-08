package ch.swisscom.mid.client.impl;

import ch.swisscom.mid.client.SignatureValidator;
import ch.swisscom.mid.client.config.ConfigurationException;
import ch.swisscom.mid.client.config.SignatureValidationConfiguration;
import ch.swisscom.mid.client.model.DataToBeSignedTXN;
import ch.swisscom.mid.client.model.SignatureValidationFailureReason;
import ch.swisscom.mid.client.model.SignatureValidationResult;
import ch.swisscom.mid.client.model.Traceable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.*;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ch.swisscom.mid.client.utils.Utils.*;
import static org.apache.commons.text.StringEscapeUtils.unescapeJava;

/**
 * Default implementation of {@link SignatureValidator}.
 *
 * @since v1.5.0
 */
public class SignatureValidatorImpl implements SignatureValidator {

    private static final Logger log = LoggerFactory.getLogger(Loggers.SIGNATURE_VALIDATOR);

    private final KeyStore validationTrustStore;
    private ObjectMapper jacksonMapper;

    public SignatureValidatorImpl(SignatureValidationConfiguration config) {
        Security.addProvider(new BouncyCastleProvider());
        this.validationTrustStore = loadValidationTruststore(config);

        jacksonMapper = new ObjectMapper();
        jacksonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public SignatureValidatorImpl(KeyStore validationTrustStore) {
        Security.addProvider(new BouncyCastleProvider());
        this.validationTrustStore = validationTrustStore;

        jacksonMapper = new ObjectMapper();
        jacksonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public SignatureValidationResult validateSignature(String base64SignatureContent, String requestedDtbs, Traceable trace) {
        assertNotEmpty(base64SignatureContent, "The base64SignatureContent parameter cannot be NULL" + printTrace(trace));
        assertNotEmpty(requestedDtbs, "The requestedDtbs parameter cannot be NULL" + printTrace(trace));

        SignatureValidationResult result = new SignatureValidationResult();
        // 4 criteria to be met in parallel to mark digital signature as valid
        result.setSignatureValid(false);
        result.setSignerCertificateValid(false);
        result.setSignerCertificatePathValid(false);
        result.setDtbsMatching(false);

        CMSSignedData cmsSignedData;
        X509Certificate signerCert = null;
        List<X509Certificate> signerCertChain;
        SignerInformation signerInfo;

        try {
            JcaX509CertificateConverter x509CertificateConverter = new JcaX509CertificateConverter();
            cmsSignedData = new CMSSignedData(Base64.getDecoder().decode(base64SignatureContent));
            // Find the signer certificate and the entire certificate chain from the CMS content
            SignerInformationStore signerInfoStore = cmsSignedData.getSignerInfos();
            signerInfo = signerInfoStore.getSigners().iterator().next();

            signerCertChain = new LinkedList<>();
            for (X509CertificateHolder currentCertHolder : cmsSignedData.getCertificates().getMatches(null)) {
                X509Certificate currentCert = x509CertificateConverter.getCertificate(currentCertHolder);
                signerCertChain.add(currentCert);
                if (signerInfo.getSID().match(currentCertHolder)) {
                    signerCert = currentCert;
                }
            }

            if (signerCert == null) {
                log.warn("Failed to extract the signing certificate from the Base64 CMS content{}", printTrace(trace));
                result.setValidationFailureReason(SignatureValidationFailureReason.FAILED_TO_EXTRACT_SIGNING_CERTIFICATE);
                return result;

            }
        } catch (Exception e) {
            log.warn("Failed to extract the signing certificate from the Base64 CMS content{}", printTrace(trace), e);
            result.setValidationException(e);
            result.setValidationFailureReason(SignatureValidationFailureReason.FAILED_TO_EXTRACT_SIGNING_CERTIFICATE);
            return result;
        }

        // extract data from the signature
        result.setMobileIdSerialNumber(extractMIDSerialNumber(signerCert));
        result.setSignedDtbs(getSignedDtbs(cmsSignedData));

        // check that the certificate is valid. It is if the current date and time are within the validity period
        // given in the certificate.
        try {
            signerCert.checkValidity();
            result.setSignerCertificateValid(true);
        } catch (CertificateExpiredException | CertificateNotYetValidException e) {
            log.warn("Failed to validate the certificate during the signature CMS content validation{}", printTrace(trace), e);
            result.setValidationException(e);
            result.setValidationFailureReason(SignatureValidationFailureReason.SIGNING_CERTIFICATE_NOT_VALID);
            return result;
        }

        try {
            // validate certificate path
            PKIXParameters params = new PKIXParameters(validationTrustStore);
            // For Mobile ID certificates (used for signatures) there is no need to check for revocation, as certificates are never revoked
            params.setRevocationEnabled(false);
            // Disable OCSP
            Security.setProperty("ocsp.enable", "false");
            // Disable CRLDP
            System.setProperty("com.sun.security.enableCRLDP", "false");
            CertPathValidator cpv = CertPathValidator.getInstance(CertPathValidator.getDefaultType());
            cpv.validate(CertificateFactory.getInstance("X.509").generateCertPath(signerCertChain), params);
            result.setSignerCertificatePathValid(true);
        } catch (Exception e) {
            log.warn("Failed to validate the certificate path during the signature CMS content validation{}", printTrace(trace), e);
            result.setValidationException(e);
            result.setValidationFailureReason(SignatureValidationFailureReason.SIGNING_CERTIFICATE_PATH_NOT_VALID);
            return result;
        }

        // verify the signature on the SignerInformation object
        try {
            result.setSignatureValid(signerInfo.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(signerCert)));
            if (!result.isSignatureValid()) {
                result.setValidationFailureReason(SignatureValidationFailureReason.SIGNATURE_VALIDATION_FAILED);
            }
        } catch (OperatorCreationException | CMSException e) {
            log.warn("Failed to validate the signature against the signer info " +
                    "during the signature CMS content validation{}", printTrace(trace), e);
            result.setValidationException(e);
            result.setValidationFailureReason(SignatureValidationFailureReason.SIGNATURE_VALIDATION_FAILED);
            return result;
        }

        // verify the DTBS from the request vs the one from the response
        if (result.getSignedDtbs() == null) {
            log.info("Failed to match the DTBS texts, requested=[{}] vs signed=[{}]{}", requestedDtbs, result.getSignedDtbs(), printTrace(trace));
            result.setValidationFailureReason(SignatureValidationFailureReason.DATA_TO_BE_SIGNED_NOT_MATCHING);
            return result;
        }
        if (requestedDtbs.startsWith("{")) {
            result.setDtbsMatching(false);
            try {
                // parse item
                String[] dtbsArray = requestedDtbs.split("\"dtbd\":");
                String reqDtbsValueStr = "";
                if (dtbsArray.length > 0) {
                    String reqDtbsValueRaw = dtbsArray[1];
                    reqDtbsValueStr = reqDtbsValueRaw.substring(0, reqDtbsValueRaw.length() - 1);
                }
                // fix response DTBS string
                String escResultDtbs = unescapeJava(result.getSignedDtbs()
                        .replace("\"format_version\"", "\\\"format_version\\\"")
                        .replace("\"content_string\"", "\\\"content_string\\\"")
                        .replace("\"[", "[")
                        .replace("]\"", "]"));

                DataToBeSignedTXN resDtbs = jacksonMapper.readValue(escResultDtbs, DataToBeSignedTXN.class);
                String finalResDtbs = jacksonMapper.writeValueAsString(resDtbs.getDtbd());
                result.setDtbsMatching(reqDtbsValueStr.equals(finalResDtbs));
            } catch (JsonProcessingException e) {
                log.info("Failed to match the DTBS texts, requested=[{}] vs signed=[{}]{}", requestedDtbs, result.getSignedDtbs(), printTrace(trace));
                result.setValidationFailureReason(SignatureValidationFailureReason.DATA_TO_BE_SIGNED_NOT_MATCHING);
            }
            return result;

        } else if (requestedDtbs.equals(result.getSignedDtbs())) {
            result.setDtbsMatching(true);
        } else {
            log.info("Failed to match the DTBS texts, requested=[{}] vs signed=[{}]{}", requestedDtbs, result.getSignedDtbs(), printTrace(trace));
            result.setValidationFailureReason(SignatureValidationFailureReason.DATA_TO_BE_SIGNED_NOT_MATCHING);
        }

        return result;
    }

    @Override
    public String getMIDSerialNumber(String base64SignatureContent, Traceable trace) {
        assertNotEmpty(base64SignatureContent, "The base64SignatureContent parameter cannot be NULL" + printTrace(trace));
        CMSSignedData cmsSignedData;
        X509Certificate signerCert = null;
        List<X509Certificate> signerCertChain;
        SignerInformation signerInfo;
        try {
            final JcaX509CertificateConverter x509CertificateConverter = new JcaX509CertificateConverter();
            cmsSignedData = new CMSSignedData(Base64.getDecoder().decode(base64SignatureContent));

            // Find the signer certificate and the entire certificate chain from the CMS content
            final SignerInformationStore signerInfoStore = cmsSignedData.getSignerInfos();
            signerInfo = signerInfoStore.getSigners().iterator().next();
            signerCertChain = new LinkedList<>();

            for (final X509CertificateHolder currentCertHolder : cmsSignedData.getCertificates().getMatches(null)) {
                X509Certificate currentCert = x509CertificateConverter.getCertificate(currentCertHolder);
                signerCertChain.add(currentCert);
                if (signerInfo.getSID().match(currentCertHolder)) {
                    signerCert = currentCert;
                }
            }
        } catch (Exception e) {
            log.warn("Failed to extract the signing certificate from the Base64 CMS content{}", printTrace(trace), e);
            return null;
        }
        return extractMIDSerialNumber(signerCert);

    }

    // ----------------------------------------------------------------------------------------------------

    private static final Pattern SERIAL_NUMBER_PATTERN = Pattern.compile(".*SERIALNUMBER=(.{16}).*");

    /**
     * Get the user's unique Mobile ID serial number from the signer certificate's SubjectDN
     *
     * @return the user's unique Mobile ID serial number.
     */
    private String extractMIDSerialNumber(X509Certificate signerCert) {
        assertNotNull(signerCert, "The signerCert param for Mobile ID serial number extraction cannot be NULL");
        Matcher matcher = SERIAL_NUMBER_PATTERN.matcher(signerCert.getSubjectX500Principal().toString().toUpperCase());
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    /**
     * Get signed content - should be equal to the DTBS Message of the origin Signature Request
     *
     * @return the signed data.
     */
    private String getSignedDtbs(CMSSignedData cmsSignedData) {
        return new String((byte[]) cmsSignedData.getSignedContent().getContent(), StandardCharsets.UTF_8);
    }

    private KeyStore loadValidationTruststore(SignatureValidationConfiguration config) {
        try {
            KeyStore trustStore = KeyStore.getInstance(config.getTrustStoreType());
            if (config.getTrustStoreFile() != null) {
                try (InputStream is = new FileInputStream(config.getTrustStoreFile())) {
                    trustStore.load(is, config.getTrustStorePassword() == null ?
                            null : config.getTrustStorePassword().toCharArray());
                }
            } else if (config.getTrustStoreClasspathFile() != null) {
                try (InputStream is = this.getClass().getResourceAsStream(config.getTrustStoreClasspathFile())) {
                    trustStore.load(is, config.getTrustStorePassword() == null ?
                            null : config.getTrustStorePassword().toCharArray());
                }
            } else {
                try (InputStream is = new ByteArrayInputStream(config.getTrustStoreBytes())) {
                    trustStore.load(is, config.getTrustStorePassword() == null ?
                            null : config.getTrustStorePassword().toCharArray());
                }
            }
            return trustStore;
        } catch (Exception e) {
            throw new ConfigurationException("Failed to initialize the digital signature validation truststore " +
                    "(Mobile ID CMS signature validator)", e);
        }
    }
}
