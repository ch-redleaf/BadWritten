package myself.badwritten.base.service.impl;

import myself.badwritten.common.model.Account;
import myself.badwritten.base.dao.AccountMapper;
import myself.badwritten.base.service.AccountService;
import myself.badwritten.common.base.BaseService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登录账号表 服务实现类
 * </p>
 *
 * @author RedLeaf
 * @since 2022-01-19
 */
@Service
public class AccountServiceImpl extends BaseService<AccountMapper, Account> implements AccountService {

}
