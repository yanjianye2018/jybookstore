package yzy.bookstore.user.service;


import yzy.bookstore.user.dao.UserDao;
import yzy.bookstore.user.domain.User;

/**
 * User业务层
 *
 * @author yzy
 */
public class UserService {
    private UserDao userDao = new UserDao();

    //注册功能
    public void regist(User form) throws UserException {
        //从user对象中获取username校验用户名是否存在
        User user = userDao.findByUsername(form.getUsername());
        if (user != null) {
            throw new UserException("用户名已经被注册!");
        }
        //校验email
        user = userDao.findByEmail(form.getEmail());
        if (user != null) {
            throw new UserException("邮箱已经被注册!");
        }

        //如果都为空说明用户信息不存在,添加到数据库中,
        userDao.add(form);//是form表单获取的对象,user为空
    }

    //查询激活状态
    public void active(String code) throws UserException {
        //使用激活码查询用户的激活状态
        User user = userDao.findByCode(code);
        //查询为空
        if (user == null) {
            throw new UserException("激活码无效!");
        }
        //激活码已存在
        if (user.isState()) {
            throw new UserException("您已经激活过了,不要重复激活!");
        }

        //修改用户激活状态为true,不是传进来的code,应该是查询出来未激活user的code
        userDao.updateState(user.getCode(), true);
    }

    //登录功能
    public User login(User form) throws UserException {
        //使用用户名查询数据库,返回的是user
        User user = userDao.findByUsername(form.getUsername());
        if(user==null){
            throw new UserException("用户名错误!");
        }
        //校验密码
        if(!user.getPassword().equals(form.getPassword())){
            throw new UserException("密码错误!");
        }
        //激活状态校验
        if(!user.isState()){
            throw new UserException("您尚未激活!");
        }

        //返回的是user,form是表单传递过来的,里面数据不完整,
        return user;
    }
}
