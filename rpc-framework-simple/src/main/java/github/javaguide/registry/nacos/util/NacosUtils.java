package github.javaguide.registry.nacos.util;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.google.common.collect.Lists;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dxu2
 * @date 2022/8/30
 */
@Slf4j
public class NacosUtils {

  private static final String DEFAULT_SERVER_ADDRESS = "127.0.0.1:8848";

  // serviceName -> address
  private static final Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();

  private static NamingService namingService;

  public static NamingService createNamingService(String registerAddress) throws NacosException {
    if (null != namingService) {
      return namingService;
    }
    String nacosServerAddress = null == registerAddress ? DEFAULT_SERVER_ADDRESS : registerAddress;
    NamingService service = NamingFactory.createNamingService(nacosServerAddress);
    log.info("create namingService, address:" + nacosServerAddress);
    namingService = service;
    return namingService;
  }


  /**
   * 注册服务
   * @param namingService 命名服务
   * @param rpcServiceName 服务名称
   * @param inetSocketAddress 地址
   * @throws NacosException
   */
  public static void registerService(NamingService namingService, String rpcServiceName, InetSocketAddress inetSocketAddress) throws NacosException {
    subscribeService(namingService, rpcServiceName);
    // register service
    namingService.registerInstance(rpcServiceName, inetSocketAddress.getHostString(), inetSocketAddress.getPort());
    log.info("register service, address:" + inetSocketAddress);
  }


  /**
   * 获取服务下的健康实例
   * @param rpcServiceName 服务名称
   * @return 服务地址列表
   * @throws NacosException
   */
  public static List<String> getAddressList(NamingService namingService, String rpcServiceName) throws NacosException {
    if (SERVICE_ADDRESS_MAP.containsKey(rpcServiceName)) {
      return SERVICE_ADDRESS_MAP.get(rpcServiceName);
    }
    List<Instance> instances = namingService.selectInstances(rpcServiceName, true);
    List<String> addressList = Lists.newArrayList();
    for (Instance instance : instances) {
      addressList.add(instance.getIp() + ":" + instance.getPort());
    }
    SERVICE_ADDRESS_MAP.put(rpcServiceName, addressList);
    return addressList;
  }

  /**
   * 监听服务 监听服务下实例列表变化
   * @param rpcServiceName 服务名称
   * @throws NacosException
   */
  public static void subscribeService(NamingService namingService, String rpcServiceName) throws NacosException {
    EventListener eventListener = new EventListener() {
      @Override
      public void onEvent(Event event) {
        if (event instanceof NamingEvent) {
          NamingEvent namingEvent = (NamingEvent) event;
          List<String> addressList = Lists.newArrayList();
          for (Instance instance : namingEvent.getInstances()) {
            addressList.add(instance.getIp() + ":" + instance.getPort());
          }
          log.info(rpcServiceName + " address change, now address:" + addressList);
          SERVICE_ADDRESS_MAP.put(rpcServiceName, addressList);
        }
      }
    };
    namingService.subscribe(rpcServiceName, eventListener);
  }





}
