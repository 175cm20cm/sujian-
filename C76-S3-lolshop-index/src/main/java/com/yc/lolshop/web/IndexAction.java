package com.yc.lolshop.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.yc.lolshop.bean.CartExample;
import com.yc.lolshop.bean.User;
import com.yc.lolshop.biz.BizException;
import com.yc.lolshop.biz.IEbuyBackAction;
import com.yc.lolshop.biz.UserBiz;
import com.yc.lolshop.dao.CartMapper;
import com.yc.lolshop.dao.CategoryMapper;

@RestController
@SessionAttributes("loginedUser")
public class IndexAction {

	@Resource
	IEbuyBackAction eba;
	
	@Resource
	private CategoryMapper cgm;
	@Resource
	UserBiz ubiz;

	/*
	 * @ModelAttribute public void init() {
	 * 
	 * }
	 */

	@GetMapping({ "/", "index", "index.html" })
	public ModelAndView index(ModelAndView mav) {
		// 通过远程服务调用方式获取分类信息
		
		mav.addObject("cclist",eba.getCc());
		if (mav.getViewName() == null) {
			mav.setViewName("index");
		}
		return mav;
	}

	@GetMapping("tologin")
	public ModelAndView tologin(ModelAndView mav) {
		mav.setViewName("login");
		return mav;
	}

	@PostMapping("login")
	public ModelAndView login(User user, ModelAndView mav, @SessionAttribute(name = "uri", required = false) String uri,
			HttpSession session) {
		try {
			User dbuser = ubiz.login(user);
			// mav.addObject("loginedUser", dbuser);

			session.setAttribute("loginedUser", dbuser);
			if (uri != null) {
				// 这是拦截登录的情况
				mav.setViewName("redirect:http://127.0.0.1" + uri);
			} else {
				// 这是用户的主动登录
				mav.setViewName("index");
			}
			return index(mav);
		} catch (BizException e) {
			e.printStackTrace();
			mav.addObject("msg", e.getMessage());
			mav.setViewName("login");
		}
		return mav;
	}

	@Resource
	private CartMapper cm;

	@GetMapping("toCart")
	public ModelAndView toCart(ModelAndView mav, @SessionAttribute("loginedUser") User user) {
		CartExample ce = new CartExample();
		ce.createCriteria().andUidEqualTo(user.getId());

		mav.addObject("clist", cm.selectByExample(ce));

		mav.setViewName("cart");
		return mav;
	}

	/*
	 * @PostMapping("login") public ModelAndView login(EasybuyUser user,
	 * ModelAndView mav,
	 * 
	 * @SessionAttribute(name="uri",required=false) String uri, HttpSession session)
	 * { try { EasybuyUser dbuser = ubiz.login(user);
	 *//**
		 * 屏蔽之前的写法 // 将用户对象添加到会话 mav.addObject("loginedUser", dbuser);
		 * 
		 * 响应重定向添加会话属性, 使用 mav.addObject("loginedUser", dbuser); 会出现会话属性添加失败的问题,
		 * 所以改成下面的写法
		 *//*
			 * session.setAttribute("loginedUser", dbuser); if(uri != null) { // 这是拦截登录的情况
			 * mav.setViewName("redirect:http://127.0.0.1" + uri); } else { // 这是用户的主动登录
			 * mav.setViewName("index"); } return index(mav); } catch (BizException e) {
			 * e.printStackTrace(); mav.addObject("msg", e.getMessage());
			 * mav.setViewName("Login"); } return mav; }
			 * 
			 * @GetMapping("product") public ModelAndView product(int id, ModelAndView mav)
			 * { // 要展示的商品 mav.addObject("product", eba.product(id)); // 商品分类
			 * mav.addObject("pclist", eba.getPc()); mav.setViewName("product"); return mav;
			 * }
			 */

	/*
	 * @GetMapping("addCart") public ModelAndView addCart(ModelAndView mav,
	 * EasybuyCart cart, @SessionAttribute("loginedUser") EasybuyUser user) {
	 * ubiz.addCart(user, cart); return toCart(mav, user); }
	 * 
	 * @Resource private EasybuyCartMapper ecm;
	 * 
	 * @GetMapping("toCart") public ModelAndView toCart(ModelAndView
	 * mav, @SessionAttribute("loginedUser") EasybuyUser user) { EasybuyCartExample
	 * ece = new EasybuyCartExample();
	 * ece.createCriteria().andUidEqualTo(user.getId()); mav.addObject("clist",
	 * ecm.selectByExample(ece)); mav.setViewName("BuyCar"); return mav; }
	 */

}