package com.ifuture.iread.security;

import com.ifuture.iread.entity.Menu;
import com.ifuture.iread.entity.Role;
import com.ifuture.iread.entity.User;
import com.ifuture.iread.repository.MenuRepository;
import com.ifuture.iread.repository.RoleRepository;
import com.ifuture.iread.repository.UserRepository;
import com.ifuture.iread.util.PinyinUtil;
import com.ifuture.iread.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by maofn on 2017/3/13.
 */
@Service("mySecurityMetadataSource")
public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    //resourceMap就是保存的所有资源和权限的集合，URL为Key，权限作为Value
    private static HashMap<String, Collection<ConfigAttribute>> resourceMap = null;

    /**
     *
     * 自定义方法，这个类放入到Spring容器后，
     * 指定init为初始化方法，从数据库中读取资源
     */
    @PostConstruct
    public void init() {
//        initAdminData();
        loadResourceDefine();
    }

    /**
     *
     * 系统初始化管理员数据
     */
    private void initAdminData() {
        //admin
        User admin = new User();
        admin.setUserName("admin");
        String md5Password = new Md5PasswordEncoder().encodePassword(
                "admin", admin.getUserName());
        admin.setPassWord(md5Password);
        admin.setId(UUIDUtil.generateUUID());
        admin.setNickName("管理员");
        admin.setPinyin(PinyinUtil.hanyuConvertToPinyin(admin.getNickName()));
        //role
        Role role = new Role("admin", "admmin", true);
        role.setId(UUIDUtil.generateUUID());
        role.setPinyin(PinyinUtil.hanyuConvertToPinyin(role.getRoleName()));
        List<Role> list = new ArrayList<Role>();
        list.add(role);
        //menu
        Menu userMana = new Menu("/user/list", "ROLE_USER", "用户管理", 1, null);
        Menu roleMana = new Menu("/role/list", "ROLE_ROLE", "角色管理", 2, null);
        Menu menuMana = new Menu("/menu/list", "ROLE_MENU", "菜单管理", 3, null);
        userMana.setPinyin(PinyinUtil.hanyuConvertToPinyin(userMana.getMenuName()));
        roleMana.setPinyin(PinyinUtil.hanyuConvertToPinyin(roleMana.getMenuName()));
        menuMana.setPinyin(PinyinUtil.hanyuConvertToPinyin(menuMana.getMenuName()));
        userMana.setId(UUIDUtil.generateUUID());
        roleMana.setId(UUIDUtil.generateUUID());
        menuMana.setId(UUIDUtil.generateUUID());
        Set<Menu> menus = new TreeSet<>();
        menus.add(userMana);
        menus.add(roleMana);
        menus.add(menuMana);
        menuRepository.save(menus);

        role.setMenus(menus);
        roleRepository.save(role);


        admin.setRoles(list);
        userRepository.save(admin);
    }

    /**
     *
     * 程序启动的时候就加载所有资源信息
     * 初始化资源与权限的映射关系
     */
    private void loadResourceDefine() {
        // 在Web服务器启动时，提取系统中的所有权限
        List<Menu> menus = menuRepository.findAll();

        //应当是资源为key， 权限为value。 资源通常为url， 权限就是那些以ROLE_为前缀的角色。 一个资源可以由多个权限来访问
        resourceMap = new HashMap<>();

        if (menus != null && menus.size() > 0) {
            for (Menu menu : menus) {
                //获取 ROLE_为前缀的代码
                String code = menu.getCode();
                //将ROLE_XXX 封装成spring的权限配置属性
                ConfigAttribute ca = new SecurityConfig(code);
                //获取url
                String url = menu.getUrl();
                //判断资源文件和权限的对应关系，如果已经存在相关的资源url，则要通过该url为key提取出权限集合，将权限增加到权限集合中
                if (resourceMap.containsKey(url)) { //如果已存在url 加入权限
                    Collection<ConfigAttribute> value = resourceMap.get(url);
                    value.add(ca);
                    resourceMap.put(url, value);
                } else {    //如果不存存在url 加入url和权限
                    Collection<ConfigAttribute> value = new ArrayList<>();
                    value.add(ca);
                    resourceMap.put(url, value);
                }
            }
        }
    }

    /**
     * 根据url查询该url所应该有的权限
     *
     * @param object    url
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // System.err.println("-----------MySecurityMetadataSource getAttributes ----------- ");
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        // System.out.println("requestUrl is " + requestUrl);
        if (resourceMap == null) {
            loadResourceDefine();
        }
        if (requestUrl.contains("?")) {
            requestUrl = requestUrl.substring(0, requestUrl.indexOf("?"));
        }
        Collection<ConfigAttribute> configAttributes = resourceMap
                .get(requestUrl);
        return configAttributes;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
