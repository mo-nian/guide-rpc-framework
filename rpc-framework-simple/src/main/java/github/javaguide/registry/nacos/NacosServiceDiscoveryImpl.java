package github.javaguide.registry.nacos;

import com.alibaba.nacos.api.naming.NamingService;
import github.javaguide.config.RpcRegistryConfig;
import github.javaguide.extension.ExtensionLoader;
import github.javaguide.loadbalance.LoadBalance;
import github.javaguide.registry.ServiceDiscovery;
import github.javaguide.registry.nacos.util.NacosUtils;
import github.javaguide.remoting.dto.RpcRequest;
import github.javaguide.utils.SpringUtil;
import java.net.InetSocketAddress;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * @author dxu2
 * @date 2022/8/30
 */
@Slf4j
public class NacosServiceDiscoveryImpl implements ServiceDiscovery {

  private LoadBalance loadBalance;

  private String registerAddress;

  @Override
  public InetSocketAddress lookupService(RpcRequest rpcRequest) {
    try {
      if (null == loadBalance) {
        RpcRegistryConfig rpcRegistryConfig = SpringUtil.getBean(RpcRegistryConfig.class);
        loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension(rpcRegistryConfig.getLoadBalance());
        registerAddress = rpcRegistryConfig.getAddress();
      }
      NamingService namingService = NacosUtils.createNamingService(registerAddress);
      List<String> addressList = NacosUtils.getAddressList(namingService, rpcRequest.getRpcServiceName());
      String address = loadBalance.selectServiceAddress(addressList, rpcRequest);
      if (StringUtils.isEmpty(address)) {
        return null;
      }
      log.info("NacosServiceDiscoveryImpl.lookupService, address:" + address);
      String[] array = address.split(":");
      return new InetSocketAddress(array[0], Integer.parseInt(array[1]));
    } catch (Exception e) {
      log.error("NacosServiceDiscoveryImpl.lookupService, request:" + rpcRequest);
    }
    return null;
  }
}
