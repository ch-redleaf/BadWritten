package myself.badwritten.common.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import myself.badwritten.common.base.BaseModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author RedLeaf
 * @since 2022-01-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_user")
public class User extends BaseModel<User> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId("USER_ID")
    private String userId;
    /**
     * 账号id
     */
    @TableField("ACCOUNT_ID")
    private String accountId;
    /**
     * 是否删除(0:未删除,1:已删除)
     */
    @TableField("IS_DELETE")
    private BigDecimal isDelete;
    /**
     * 姓名
     */
    @TableField("USER_NAME")
    private String userName;
    /**
     * 手机号
     */
    @TableField("MOBILE")
    private String mobile;
    /**
     * 邮箱
     */
    @TableField("EMAIL")
    private String email;
    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;
    /**
     * 性别（0女生，1男生）
     */
    @TableField("SEX")
    private BigDecimal sex;
    /**
     * 是否位默认联系人
     */
    @TableField("IS_DEFAULT_CONTACT")
    private BigDecimal isDefaultContact;
    /**
     * 用户电话
     */
    @TableField("USER_TEL")
    private String userTel;
    /**
     * 用户传真
     */
    @TableField("USER_FAX")
    private String userFax;


    @Override
    protected Serializable pkVal() {
        return this.userId;
    }

}
