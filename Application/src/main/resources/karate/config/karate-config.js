function fn() {


  karate.log(karate.getProperties())
  var config=read(karate.properties['e2e.base_dir']+'config/config.json')
  karate.log(config)


//  var result = karate.callSingle('classpath:e2e/lifecycle/authorization/auth.feature');
//
//  config.token= result.accessToken;
//
//
//
//  // config.auth = {accessToken:result.accessToken}
//  karate.log('print log token is: ',config.token);
//  var LM = Java.type('OAuthLogModifier');
//  karate.configure('logModifier', LM.INSTANCE);
//  karate.configure('connectTimeout', 10000);
//  karate.configure('readTimeout', 600000);
//  return config;
}
