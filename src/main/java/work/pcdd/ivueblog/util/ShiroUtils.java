package work.pcdd.ivueblog.util;

import org.apache.shiro.SecurityUtils;
import work.pcdd.ivueblog.shiro.AccountProfile;

/**
 * @author pcdd
 */
public class ShiroUtils {

    public static AccountProfile getProfile() {
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }

}
