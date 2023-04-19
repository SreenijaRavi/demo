function() {
  //set up runtime variables based on environment
  //get system property 'karate.env'
  var env = karate.env;
  if (!env) { env = 'dev'; }  // default when karate.env not set

  // base config
  var config = {
    env: env,
   // baseUrl: 'https://aps-hopstream-api.default.abattery.appbattery.nss1.tn.akamai.com',
    //baseUrl2: 'https://aps-pliny-api.default.abattery.appbattery.nss1.tn.akamai.com',
    keyStorePath : 'classpath:certs/srrav-testnet.p12',
    keyStorePasword : '123456'
  }

  karate.log('karate.env =', karate.env);
  karate.log('config.baseUrl =', config.baseUrl);

  return config;
}
