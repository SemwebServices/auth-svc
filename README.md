


# Config

Your grails yaml config file will need to have a section like

authProviders:
  google:
    name: Google
    btnClasses: btn btn-block btn-social btn-google
    type: oidc
    clientId: --clientid--
    clientSecret: --secret--
    discoveryUrl: https://accounts.google.com/.well-known/openid-configuration
    authorization_endpoint: https://accounts.google.com/o/oauth2/v2/auth
    token_endpoint: https://www.googleapis.com/oauth2/v4/token
    userinfo_endpoint: https://www.googleapis.com/oauth2/v3/userinfo
    jwks_uri: https://www.googleapis.com/oauth2/v3/certs



