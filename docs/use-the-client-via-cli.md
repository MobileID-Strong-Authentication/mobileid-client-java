# Use the Mobile ID client via CLI

As part of the _mobileid-client-usage_ module, a command line interface (CLI) is implemented to help with running the Mobile ID client
from the command line. This turns the client into a tool that can be used for various use cases.

## Get the package

Going to the _Releases_ section of this repository, you can download the latest version of the Mobile ID client, packaged in a form that
provides the client JARs, all the needed dependencies and two scripts (batch file and shell script) for running the client on Windows
and Linux-based platforms.

## Usage

Running the client without any arguments or with the _-help_ argument outputs this content:

```text
--------------------------------------------------------------------------------
Swisscom Mobile ID Java client <versionInfo>
--------------------------------------------------------------------------------
Usage: ./bin/mid-client.sh [ARGUMENTS]

Arguments:
    -init                                               - Create sample configuration files in the current folder

    -config=my-config.properties                        - The properties file that provides the extra configuration parameters.
                                                          Use -init to create a sample file. If you don't specify a file,
                                                          by default config.properties is looked for in the start directory

    -profile-query                                      - Run a profile query on the target MSISDN. Cannot be used together with -sign

    -sign                                               - Request a digital signature to the target MSISDN. Arguments such as -lang, -dtbs
                                                          can customize what is displayed to the user and what is the signed content.
                                                          Cannot be used together with -profile-query

    -sync                                               - For sign operation. Run the signature in synchronous mode (default is async)

    -async                                              - For sign operation. Run the signature in asynchronous (polling) mode (this is the default)

    -receipt                                            - For sign operation. Send a receipt after the signature is acquired successfully

    -msisdn=41790000000                                 - The target MSISDN for the chosen operation

    -lang=en|de|it|fr                                   - The language to use during the talk with the mobile user. Choose from "en", "de", "it", "fr"

    -dtbs="Do you want to login?"                       - The data to be signed (text). Make sure to use quotes around the entire argument,
                                                          since this text will usually contain spaces.
                                                          By default this argument is "Test: Do you want to login?"

    -req-timeout                                        - Set the signature request timeout, in seconds. The default value for this parameter is 80.

    -rest                                               - Use the REST interface. Cannot be used together with -soap. This is the default interface

    -soap                                               - Use the SOAP interface. Cannot be used together with -rest (default is REST)

    -help                                               - This help text

    -v                                                  - Be verbose about what is going on (sets Logback config to info)

    -vv                                                 - Be EXTRA verbose about what is going on (sets Logback config to debug, without HTTP and TLS traffic)

    -vvv                                                - Be EXTRA-EXTRA verbose about what is going on (sets Logback config to debug, with HTTP and TLS traffic)

Notes:
    - for the arguments that take values, you can either use the form [-param=value] or [-param value].
      E.g.: ./bin/mid-client.sh -sign -msisdn=41790000000 ...... OR
            ./bin/mid-client.sh -sign -msisdn 41790000000 ......

Use cases:
    - ./bin/mid-client.sh -init                        => Have the config files generated for you in the current folder (optional step)
    - ./bin/mid-client.sh -profile-query -msisdn=41790000000
    - ./bin/mid-client.sh -profile-query -msisdn=41790000000 -soap
    - ./bin/mid-client.sh -sign -receipt -msisdn=41790000000 -lang=en -dtbs="Do you want to login?" -req-timeout=120
    - ./bin/mid-client.sh -sign -sync -receipt -msisdn=41790000000 -lang=en -dtbs="Do you want to login?" -soap -vv
    - ./bin/mid-client.sh \
           -config=my-config.properties \
           -sign -sync -receipt \
           -msisdn=41790000000 \
           -lang=en -dtbs="Do you want to login?" \
           -soap -vv
```

Use the _-init_ parameter to create a set of initial configuration files in the local directory. This files can then be customized/replaced
for your own case.

Use the _-v_, _-vv_ and _-vvv_ arguments for increasingly detailed log levels, including (for the _-vvv_ one) detailed logging of TLS and HTTP
exchanged packets.

## Examples
Start with a fresh set of configuration files:
```shell
./bin/mid-client.sh -init 
```

Get the profile information for a particular phone number (MSISDN) that your application provider is controlling:
```shell
./bin/mid-client.sh -profile-query -msisdn 41790000000 
```

Get the same profile information using a particular configuration file and the SOAP interface of Mobile ID:
```shell
./bin/mid-client.sh -profile-query -msisdn 41790000000 -config local-config.properties -soap 
```

Request a digital signature to a particular phone number (MSISDN), in sync mode:
```shell
./bin/mid-client.sh -sign -msisdn=41790000000 -lang=en -dtbs "Do you want to login?" -sync  
```

Request a digital signature to a particular phone number (MSISDN), in async mode (this is the default mode) and with signature receipt:
```shell
./bin/mid-client.sh -sign -msisdn=41790000000 -lang=en -dtbs "Do you want to login?" -receipt -req-timeout 120  
```

Note: when working with arguments that have values (such as _-msisdn_) you can pass the value either as the next argument:
```shell
./bin/mid-client.sh -sign -msisdn 41790000000
```
or in the form _name=value_:
```shell
./bin/mid-client.sh -sign -msisdn=41790000000
```
Please note that the _-dtbs_ argument is a bit more special, as it will most likely contain spaces, so either the entire name=value
construct is enclosed in double quotes or, if you use the name<space>value form, then the value is enclosed in double quotes:
```shell
./bin/mid-client.sh -sign -msisdn=41790000000 -dtbs "Do you want to login?"
./bin/mid-client.sh -sign -msisdn=41790000000 "-dtbs=Do you want to login?"
```
