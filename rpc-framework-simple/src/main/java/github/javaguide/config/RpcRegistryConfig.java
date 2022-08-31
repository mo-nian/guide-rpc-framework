package github.javaguide.config;

import lombok.Data;

/**
 * @author dxu2
 * @date 2022/8/30
 */
@Data
public class RpcRegistryConfig {

  private String protocol;

  private String address;

  private String compress;

  private String loadBalance;

  private String transport;

  private String serializer;

}
