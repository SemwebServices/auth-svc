


# Config

## Database

become the postgres OS user and start the postgres client

    psql

    postgres=# create database semweb;
    postgres=# CREATE USER semweb WITH PASSWORD '...';
    postgres=# GRANT ALL PRIVILEGES ON DATABASE semweb to semweb;



## Pointing at your local config file

THe app needs a config file with details of local certs etc. Normally (tomcat) this is done by creating a
TOMCAT_HOME/conf/Catalina/localhost/auth.xml with contents like

    <Context>
      <Environment name="spring.config.location" value="/some/path/.grails/auth.yaml" type="java.lang.String"/>
    </Context>



Your grails auth.yaml config file will need to have a section like

    systemId: aa223344
    
    authProviders:
      google:
        name: Google
        btnClasses: btn btn-block btn-social btn-google
        type: oidc
        clientId: --clientid--
        clientSecret: --clientsecret--
        discoveryUrl: https://accounts.google.com/.well-known/openid-configuration
        authorization_endpoint: https://accounts.google.com/o/oauth2/v2/auth
        token_endpoint: https://www.googleapis.com/oauth2/v4/token
        userinfo_endpoint: https://www.googleapis.com/oauth2/v3/userinfo
        jwks_uri: https://www.googleapis.com/oauth2/v3/certs
    
    jwk:
      notes: Generated using https://mkjwk.org/
      kty: RSA
      d:  ----
      e:  ----
      use:  ----
      kid:  ----
      alg:  ----
      n: ----



