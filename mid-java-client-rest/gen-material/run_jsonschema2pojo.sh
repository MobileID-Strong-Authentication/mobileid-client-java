#!/usr/bin/env bash

generate_pojo() {
    the_file=$1
    the_package=$2
    echo "Generating POJOs from: '$the_file' in package: '$the_package' ..."
    jsonschema2pojo.bat \
      --source $the_file \
      --source-type JSON \
      --target gen/src/main/java \
      --package $the_package \
      --omit-hashcode-and-equals \
      --output-encoding UTF-8 \
      --target-language JAVA \
      --generate-builders
}

generate_pojo MSS_Fault.json ch.swisscom.mid.client.rest.model.fault
generate_pojo MSS_Signature_Request.json ch.swisscom.mid.client.rest.model.signreq
generate_pojo MSS_Signature_Response.json ch.swisscom.mid.client.rest.model.signresp
generate_pojo MSS_Status_Request.json ch.swisscom.mid.client.rest.model.statusreq
generate_pojo MSS_Status_Response.json ch.swisscom.mid.client.rest.model.statusresp
generate_pojo MSS_Receipt_Request.json ch.swisscom.mid.client.rest.model.receiptreq
generate_pojo MSS_Receipt_Response.json ch.swisscom.mid.client.rest.model.receiptresp
generate_pojo MSS_ProfileQuery_Request.json ch.swisscom.mid.client.rest.model.profqreq
generate_pojo MSS_ProfileQuery_Response.json ch.swisscom.mid.client.rest.model.profqresp
