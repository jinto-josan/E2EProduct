function fn() {
  var config=read(karate.properties["e2e.base_path"]+"config/config.json")
  config.base_path=karate.properties["e2e.base_path"];
  //By default values are taken from config-urls.json
  karate.merge(config, read(config.base_path+"config/config.json"));
  if(karate.properties["e2e.env"]){
  //If env is passed from command line, values are taken from config-<env>.json and are merged
    config.env=karate.properties["e2e.env"];
    config=karate.merge(config, read(config.base_path+"config/config-"+karate.properties["e2e.env"]+".json"));
  }

  karate.log(config)
  return config;
}
