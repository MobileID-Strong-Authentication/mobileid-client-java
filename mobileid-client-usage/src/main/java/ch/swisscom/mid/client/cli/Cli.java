package ch.swisscom.mid.client.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.swisscom.mid.client.MIDClient;
import ch.swisscom.mid.client.MIDClientException;
import ch.swisscom.mid.client.config.ClientConfiguration;
import ch.swisscom.mid.client.config.HttpConfiguration;
import ch.swisscom.mid.client.config.TlsConfiguration;
import ch.swisscom.mid.client.config.UrlsConfiguration;
import ch.swisscom.mid.client.impl.Loggers;
import ch.swisscom.mid.client.impl.MIDClientImpl;
import ch.swisscom.mid.client.model.*;

import static ch.swisscom.mid.client.samples.Utils.prettyPrintTheException;

/**
 * Command line interface for the Mobile ID client. Allows the running of the MID Client from the command line, with most of
 * the configuration parameters read from a properties file and only the most important parameters given through the command arguments.
 *
 * Examples:
 * ./mid-client.sh -help
 * ./mid-client.sh -profile-query -msisdn=4071111111111
 * ./mid-client.sh -sign -sync -msisdn=4071111111111 -lang=en "-dtbs=Do you want to login?" -receipt
 * ./mid-client.sh -sign -async -msisdn=4071111111111 -lang=en "-dtbs=Do you want to login?" -receipt
 * ./mid-client.sh -sign -async -msisdn 4071111111111 -lang en -dtbs "Do you want to login?" -receipt
 */
public class Cli {

    private static final Logger logClient = LoggerFactory.getLogger(Loggers.LOGGER_CLIENT);

    public static final String SEPARATOR = "--------------------------------------------------------------------------------";
    private static final String PARAM_CONFIG = "config";
    private static final String PARAM_INIT = "init";
    private static final String PARAM_PROFILE_QUERY = "profile-query";
    private static final String PARAM_SIGN = "sign";
    private static final String PARAM_SYNC = "sync";
    private static final String PARAM_ASYNC = "async";
    private static final String PARAM_RECEIPT = "receipt";
    private static final String PARAM_LANG = "lang";
    private static final String PARAM_MSISDN = "msisdn";
    private static final String PARAM_DTBS = "dtbs";
    private static final String PARAM_REST = "rest";
    private static final String PARAM_SOAP = "soap";
    private static final String PARAM_HELP = "help";

    private static final String PARAM_VERBOSE1 = "v";
    private static final String PARAM_VERBOSE2 = "vv";
    private static final String PARAM_VERBOSE3 = "vvv";
    private static final String HEADER = "Swisscom Mobile ID Java client ";

    private static final String OPERATION_SIGN = "sign";
    private static final String OPERATION_PROFILE_QUERY = "profile-query";

    private static final String INTERFACE_REST = "rest";
    private static final String INTERFACE_SOAP = "soap";

    // ----------------------------------------------------------------------------------------------------

    private static ClientVersionProvider versionProvider;
    private static boolean continueExecution;
    private static String startFolder;

    private static String configFile;
    private static String operation;
    private static String lang = "en";
    private static String msisdn;
    private static String dtbs = "Test: Do you want to login?";
    private static final String receiptDtbd = "Login completed successfully";
    private static boolean syncSignature = false;
    private static boolean sendReceipt = false;
    private static String interfaceType;
    private static int verboseLevel;

    public static void main(String[] args) {
        versionProvider = new ClientVersionProvider();
        versionProvider.init();
        startFolder = new File("").getAbsolutePath();

        parseArguments(args);
        if (!continueExecution) {
            return;
        }

        configureLogback();
        printStartupParameters();
        Properties properties = loadConfigProperties();
        PrettyPrintingTrafficObserver prettyPrinterTrafficObserver = new PrettyPrintingTrafficObserver();
        ObjectMapper jacksonMapper = new ObjectMapper();
        jacksonMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setApId(properties.getProperty("client.msspApId"));
        clientConfig.setApPassword(properties.getProperty("client.msspApPassword"));
        if (interfaceType.equals(INTERFACE_REST)) {
            clientConfig.setProtocolToRest();
            UrlsConfiguration urls = clientConfig.getUrls();
            urls.setAllServiceUrlsTo(properties.getProperty("server.rest.url"));
        } else {
            clientConfig.setProtocolToSoap();
            UrlsConfiguration urls = clientConfig.getUrls();
            urls.setSignatureServiceUrl(properties.getProperty("server.soap.signatureUrl"));
            urls.setStatusQueryServiceUrl(properties.getProperty("server.soap.statusQueryUrl"));
            urls.setReceiptServiceUrl(properties.getProperty("server.soap.receiptUrl"));
            urls.setProfileQueryServiceUrl(properties.getProperty("server.soap.profileQueryUrl"));
        }

        TlsConfiguration tls = clientConfig.getTls();
        tls.setKeyStoreFile(properties.getProperty("client.keyStore.file"));
        tls.setKeyStorePassword(properties.getProperty("client.keyStore.password"));
        tls.setKeyStoreKeyPassword(properties.getProperty("client.keyStore.keyPassword"));
        tls.setKeyStoreCertificateAlias(properties.getProperty("client.keyStore.certAlias"));
        tls.setTrustStoreFile(properties.getProperty("server.trustStore.file"));
        tls.setTrustStorePassword(properties.getProperty("server.trustStore.password"));
        tls.setHostnameVerification(Boolean.parseBoolean(properties.getProperty("server.hostnameVerification")));

        HttpConfiguration http = clientConfig.getHttp();
        http.setConnectionTimeoutInMs(Integer.parseInt(properties.getProperty("client.http.connectionTimeoutInSeconds")) * 1000);
        http.setResponseTimeoutInMs(Integer.parseInt(properties.getProperty("client.http.responseTimeoutInSeconds")) * 1000);

        String finalResult = null;

        try (MIDClient midClient = new MIDClientImpl(clientConfig)) {
            if (operation.equals(OPERATION_PROFILE_QUERY)) {
                ProfileRequest profileRequest = new ProfileRequest();
                profileRequest.getMobileUser().setMsisdn(msisdn);
                profileRequest.setExtensionParamsToAllValues();
                profileRequest.setTrafficObserver(prettyPrinterTrafficObserver);

                ProfileResponse response = midClient.requestProfile(profileRequest);
                finalResult = "Profile response:\n" + jacksonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
            } else {
                SignatureRequest request = new SignatureRequest();
                request.setUserLanguage(UserLanguage.getByValue(lang));
                request.getDataToBeSigned().setData(dtbs);
                request.getMobileUser().setMsisdn(msisdn);
                request.setSignatureProfile(SignatureProfiles.ANY_LOA4);
                request.addAdditionalService(new GeofencingAdditionalService());
                request.setTrafficObserver(prettyPrinterTrafficObserver);

                SignatureResponse response;
                if (syncSignature) {
                    response = midClient.requestSyncSignature(request);
                } else {
                    response = midClient.requestAsyncSignature(request);
                    while (response.getStatus().getStatusCode() == StatusCode.REQUEST_OK ||
                           response.getStatus().getStatusCode() == StatusCode.OUTSTANDING_TRANSACTION) {
                        //noinspection BusyWait
                        Thread.sleep(5000);
                        response = midClient.pollForSignatureStatus(response.getTracking());
                    }
                }
                System.out.println(response.toString());

                if (sendReceipt && response.getStatus().getStatusCode() == StatusCode.SIGNATURE) {
                    ReceiptRequest receiptRequest = new ReceiptRequest();
                    receiptRequest.setStatusCode(StatusCode.REQUEST_OK);
                    receiptRequest.getMessageToBeDisplayed().setData(receiptDtbd);
                    receiptRequest.getRequestExtension().getReceiptProfile().setLanguage(lang);
                    receiptRequest.setTrafficObserver(prettyPrinterTrafficObserver);

                    ReceiptResponse receiptResponse = midClient.requestSyncReceipt(response.getTracking(), receiptRequest);
                    finalResult = "Receipt response:\n" + jacksonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(receiptResponse);
                } else {
                    finalResult = "Signature response:\n" + jacksonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(SEPARATOR);
            System.out.println(prettyPrintTheException(e));
        }

        if (finalResult != null) {
            System.out.println(SEPARATOR);
            System.out.println(finalResult);
            System.out.println(SEPARATOR);
        }
    }

    // ----------------------------------------------------------------------------------------------------

    private static void parseArguments(String[] args) {
        if (args.length == 0) {
            showHelp(null);
            return;
        }
        int argIndex = 0;
        while (argIndex < args.length) {
            String argName = args[argIndex];
            String argValue = null;
            if (argName.startsWith("--")) {
                argName = argName.substring(2);
            }
            if (argName.startsWith("-")) {
                argName = argName.substring(1);
            }
            int equalSignPos = argName.indexOf('=');
            if (equalSignPos > 0) {
                argValue = argName.substring(equalSignPos + 1);
                argName = argName.substring(0, equalSignPos);
            }
            switch (argName) {
                case PARAM_HELP: {
                    showHelp(null);
                    return;
                }
                case PARAM_INIT: {
                    runInit();
                    return;
                }
                case PARAM_VERBOSE1: {
                    if (verboseLevel < 1) {
                        verboseLevel = 1;
                    }
                    break;
                }
                case PARAM_VERBOSE2: {
                    if (verboseLevel < 2) {
                        verboseLevel = 2;
                    }
                    break;
                }
                case PARAM_VERBOSE3: {
                    if (verboseLevel < 3) {
                        verboseLevel = 3;
                    }
                    break;
                }
                case PARAM_CONFIG: {
                    if (argValue == null) {
                        if (argIndex + 1 < args.length) {
                            configFile = args[argIndex + 1];
                            argIndex++;
                        } else {
                            showHelp("Configuration file name is missing");
                        }
                    } else {
                        configFile = argValue;
                    }
                    break;
                }
                case PARAM_SIGN: {
                    if (operation != null) {
                        showHelp("More than one operation selector was found in the calling arguments. "
                                 + "Use either -" + PARAM_SIGN + " or -" + PARAM_PROFILE_QUERY);
                        return;
                    }
                    operation = OPERATION_SIGN;
                    break;
                }
                case PARAM_PROFILE_QUERY: {
                    if (operation != null) {
                        showHelp("More than one operation selector was found in the calling arguments. "
                                 + "Use either -" + PARAM_SIGN + " or -" + PARAM_PROFILE_QUERY);
                        return;
                    }
                    operation = OPERATION_PROFILE_QUERY;
                    break;
                }
                case PARAM_SYNC: {
                    syncSignature = true;
                    break;
                }
                case PARAM_ASYNC: {
                    syncSignature = false;
                    break;
                }
                case PARAM_RECEIPT: {
                    sendReceipt = true;
                    break;
                }
                case PARAM_MSISDN: {
                    if (argValue == null) {
                        if (argIndex + 1 < args.length) {
                            msisdn = args[argIndex + 1];
                            argIndex++;
                        } else {
                            showHelp("MSISDN is missing");
                        }
                    } else {
                        msisdn = argValue;
                    }
                    break;
                }
                case PARAM_LANG: {
                    if (argValue == null) {
                        if (argIndex + 1 < args.length) {
                            lang = args[argIndex + 1];
                            argIndex++;
                        } else {
                            showHelp("Language is missing (the lang parameter)");
                        }
                    } else {
                        lang = argValue;
                    }
                    break;
                }
                case PARAM_DTBS: {
                    if (argValue == null) {
                        if (argIndex + 1 < args.length) {
                            dtbs = args[argIndex + 1];
                            argIndex++;
                        } else {
                            showHelp("Data to be signed is missing (the dtbs parameter)");
                        }
                    } else {
                        dtbs = argValue;
                    }
                    break;
                }
                case PARAM_REST: {
                    if (interfaceType != null) {
                        showHelp("More than one interface selector was found in the calling arguments. "
                                 + "Use either -" + PARAM_REST + " or -" + PARAM_SOAP);
                        return;
                    }
                    interfaceType = INTERFACE_REST;
                    break;
                }
                case PARAM_SOAP: {
                    if (interfaceType != null) {
                        showHelp("More than one interface selector was found in the calling arguments. "
                                 + "Use either -" + PARAM_REST + " or -" + PARAM_SOAP);
                        return;
                    }
                    interfaceType = INTERFACE_SOAP;
                    break;
                }
            }
            argIndex++;
        }
        if (operation == null) {
            showHelp("Operation selection is missing. Use either -" + PARAM_SIGN + " or -" + PARAM_PROFILE_QUERY);
            return;
        }
        if (msisdn == null) {
            showHelp("MSISDN is missing");
            return;
        }
        if (interfaceType == null) {
            interfaceType = INTERFACE_REST;
        }
        if (configFile == null) {
            configFile = "config.properties";
        }
        continueExecution = true;
    }

    private static void showHelp(String argsValidationError) {
        if (argsValidationError != null) {
            System.out.println(argsValidationError);
        }
        String usageText = copyFileFromClasspathToString("/cli-files/usage.txt");
        if (versionProvider.isVersionInfoAvailable()) {
            usageText = usageText.replace("${versionInfo}", "- " + versionProvider.getVersionInfo());
        }
        System.out.println(usageText);
    }

    private static void runInit() {
        String[][] configPairs = new String[][]{
            new String[]{"/cli-files/config-sample.properties", "config.properties"},
            new String[]{"/cli-files/keystore.jks", "keystore.jks"},
            new String[]{"/cli-files/truststore.jks", "truststore.jks"}
        };
        for (String[] configPair : configPairs) {
            String inputFile = configPair[0];
            String baseOutputFile = configPair[1];
            String outputFile = startFolder + "/" + baseOutputFile;
            if (new File(outputFile).exists()) {
                System.out.println("File " + baseOutputFile + " already exists! Will not be overridden!");
                return;
            }
            System.out.println("Writing " + baseOutputFile + " to " + outputFile);
            copyFileFromClasspathToDisk(inputFile, outputFile);
        }
    }

    private static void configureLogback() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        switch (verboseLevel) {
            case 0: {
                setLoggerToLevel(Logger.ROOT_LOGGER_NAME, "warn", loggerContext);
                setLoggerToLevel("org.apache.hc", "info", loggerContext);
                setLoggerToLevel(Loggers.LOGGER_CLIENT, "info", loggerContext);
                setLoggerToLevel(Loggers.LOGGER_CONFIG, "info", loggerContext);
                setLoggerToLevel(Loggers.LOGGER_CLIENT_PROTOCOL, "info", loggerContext);
                setLoggerToLevel(Loggers.LOGGER_REQUEST_RESPONSE, "warn", loggerContext);
                setLoggerToLevel(Loggers.LOGGER_FULL_REQUEST_RESPONSE, "warn", loggerContext);
                break;
            }
            case 1: {
                setLoggerToLevel(Logger.ROOT_LOGGER_NAME, "info", loggerContext);
                setLoggerToLevel("org.apache.hc", "info", loggerContext);
                setLoggerToLevel(Loggers.LOGGER_CLIENT, "info", loggerContext);
                setLoggerToLevel(Loggers.LOGGER_CONFIG, "info", loggerContext);
                setLoggerToLevel(Loggers.LOGGER_CLIENT_PROTOCOL, "info", loggerContext);
                setLoggerToLevel(Loggers.LOGGER_REQUEST_RESPONSE, "debug", loggerContext);
                setLoggerToLevel(Loggers.LOGGER_FULL_REQUEST_RESPONSE, "warn", loggerContext);
                break;
            }
            case 2: // falls through
            case 3: {
                setLoggerToLevel(Logger.ROOT_LOGGER_NAME, "debug", loggerContext);
                setLoggerToLevel(Loggers.LOGGER_CLIENT, "debug", loggerContext);
                setLoggerToLevel(Loggers.LOGGER_CONFIG, "debug", loggerContext);
                setLoggerToLevel(Loggers.LOGGER_CLIENT_PROTOCOL, "debug", loggerContext);
                setLoggerToLevel(Loggers.LOGGER_REQUEST_RESPONSE, "warn", loggerContext);
                setLoggerToLevel(Loggers.LOGGER_FULL_REQUEST_RESPONSE, "debug", loggerContext);
                if (verboseLevel == 2) {
                    setLoggerToLevel("org.apache.hc", "info", loggerContext);
                } else {
                    setLoggerToLevel("org.apache.hc", "trace", loggerContext);
                }
                break;
            }
            default: {
                throw new IllegalStateException("Invalid verboseLevel: " + verboseLevel);
            }
        }
    }

    private static void setLoggerToLevel(String loggerName, String level, LoggerContext loggerContext) {
        ch.qos.logback.classic.Logger logger = loggerContext.getLogger(loggerName);
        if (logger != null) {
            logger.setLevel(Level.toLevel(level));
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static String copyFileFromClasspathToString(String inputFile) {
        ByteArrayOutputStream baos = null;
        InputStream is = null;
        try {
            baos = new ByteArrayOutputStream();
            is = Cli.class.getResourceAsStream(inputFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) > 0) {
                baos.write(buffer, 0, bytesRead);
            }
            closeStream(baos);
            return new String(baos.toByteArray(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new MIDClientException("Failed to copy the file: [" + inputFile + "] to string");
        } finally {
            closeStream(baos);
            closeStream(is);
        }
    }

    private static void copyFileFromClasspathToDisk(String inputFile, String outputFile) {
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            fos = new FileOutputStream(outputFile);
            is = Cli.class.getResourceAsStream(inputFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) > 0) {
                fos.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new MIDClientException("Failed to create the file: [" + outputFile + "]");
        } finally {
            closeStream(is);
            closeStream(fos);
        }
    }

    private static void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {
                logClient.warn("Failed to close stream of type " + closeable.getClass().getName());
            }
        }
    }

    private static void printStartupParameters() {
        System.out.println(SEPARATOR);
        if (versionProvider.isVersionInfoAvailable()) {
            System.out.println(HEADER + versionProvider.getVersionInfo());
        } else {
            System.out.println(HEADER);
        }
        System.out.println(SEPARATOR);
        System.out.println("Starting with following parameters:");
        System.out.println("Operation             : " + operation);
        System.out.println("MSISDN                : " + msisdn);
        System.out.println("Config                : " + configFile);
        System.out.println("Interface             : " + interfaceType);
        if (operation.equals(OPERATION_SIGN)) {
            System.out.println("Async operation       : " + (!syncSignature));
            System.out.println("Language              : " + lang);
            System.out.println("DTBS                  : " + dtbs);
            System.out.println("Send receipt          : " + sendReceipt);
        }
        System.out.println("Verbose level         : " + verboseLevel);
        System.out.println(SEPARATOR);
    }

    private static Properties loadConfigProperties() {
        InputStream is = null;
        try {
            is = new FileInputStream(configFile);
            Properties properties = new Properties();
            properties.load(is);
            return properties;
        } catch (IOException exception) {
            throw new MIDClientException("Failed to load configuration properties from " + configFile, exception);
        } finally {
            closeStream(is);
        }
    }
}
