package github.javaguide.registry.nacos;

import com.alibaba.nacos.api.naming.NamingService;
import github.javaguide.registry.ServiceRegistry;
import github.javaguide.registry.nacos.util.NacosUtils;
import java.net.InetSocketAddress;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dxu2
 * @date 2022/8/30
 */
@Slf4j
public class NacosServiceRegistryImpl implements ServiceRegistry {

  @Override
  public void registerService(String rpcServiceName, String registerAddress, InetSocketAddress inetSocketAddress) {
    try {
      NamingService namingService = NacosUtils.createNamingService(registerAddress);
      NacosUtils.registerService(namingService, rpcServiceName, inetSocketAddress);
    } catch (Exception e) {
      log.error("NacosServiceRegistryImpl.namingService error", e);
    }
  }
}
