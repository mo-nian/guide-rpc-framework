package github.javaguide.spring;

import github.javaguide.annotation.RpcConfig;
import github.javaguide.annotation.RpcConfigs;
import github.javaguide.config.RpcRegistryConfig;

/**
 * @author dxu2
 * @date 2022/8/30
 */
public class RpcConfigConfiguration {

  @RpcConfigs({
      @RpcConfig(prefix = "rpc.config", type = RpcRegistryConfig.class)
  })
  public static class Config {

  }

}
