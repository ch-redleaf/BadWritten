package myself.badwritten.common.model;

import java.io.Serializable;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import myself.badwritten.common.base.BaseModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 登录账号表
 * </p>
 *
 * @author RedLeaf
 * @since 2022-01-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_account")
public class Account extends BaseModel<Account> {

    private static final long serialVersionUID = 1L;

    /**
     * 账号id
     */
    @TableId("ACCOUNT_ID")
    private String accountId;
    /**
     * 登录名
     */
    @TableField("ACCOUNT_NAME")
    private String accountName;
    @TableField("ACCOUNT_TEL")
    private String accountTel;
    @TableField("ACCOUNT_EMAIL")
    private String accountEmail;
    /**
     * 密码
     */
    @TableField("ACCOUNT_PASSWORD")
    private String accountPassword;
    /**
     * 是否可用(0:不可用,1:可用)
     */
    @TableField("IS_ENABLE")
    private BigDecimal isEnable;
    /**
     * 是否激活(0:否,1:是)
     */
    @TableField("IS_ACTIVE")
    private BigDecimal isActive;
    /**
     * 微信id
     */
    @TableField("OPEN_ID")
    private String openId;


    @Override
    protected Serializable pkVal() {
        return this.accountId;
    }

}
