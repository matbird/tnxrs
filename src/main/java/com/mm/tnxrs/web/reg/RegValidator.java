package com.mm.tnxrs.web.reg;

import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.validate.Validator;

public class RegValidator extends Validator{

	@Override
	protected void validate(Controller c) {
		setShortCircuit(true);

		/*if (SensitiveWordsKit.checkSensitiveWord(c.getPara("nickName")) != null) {
			addError("nickNameMsg", "昵称不能包含敏感词");
		}*/
		
		validateRequired("nickName", "nickNameMsg", "昵称不能为空");
		validateString("nickName", 1, 19, "nickNameMsg", "昵称不能超过19个字");
		String nickName = c.getPara("nickName").trim();
		if (nickName.contains("@") || nickName.contains("＠")) { // 全角半角都要判断
			addError("nickNameMsg", "昵称不能包含 \"@\" 字符");
		}
		if (nickName.contains(" ") || nickName.contains("　")) {
			addError("nickNameMsg", "昵称不能包含空格");
		}
		if (RegService.me.isNickNameExists(c.getPara("nickName"))) {
			addError("nickNameMsg", "昵称已被注册");
		}
		Ret ret = validateNickName(nickName);
		
		if (ret.isFail()) {
			addError("nickNameMsg", ret.getStr("msg"));
		}

		validateRequired("userName", "userNameMsg", "邮箱不能为空");
		validateEmail("userName", "userNameMsg", "邮箱格式不正确");
		if(RegService.me.isUserNameExists(c.getPara("userName"))) {
			addError("userNameMsg", "邮箱已被注册");
		}
		
		validateString("password", 6, 100, "passwordMsg", "密码长度不能小于6");

		// 验证码校验放在最后，在 shortCircuit 模式下避免刷新验证码，也即避免重复输入验证码
		validateCaptcha("captcha", "captchaMsg", "验证码不正确");
	}

	@Override
	protected void handleError(Controller c) {
		c.renderJson();
	}

	/**
	 * TODO 用正则来匹配这些不能使用的字符，而不是用这种 for + contains 这么土的办法
	 *    初始化的时候仍然用这个数组，然后用 StringBuilder 来个 for 循环拼成如下的形式：
	 *    regex = "( |`|~|!|......|\(|\)|=|\[|\]|\?|<|>\。|\,)"
	 *    直接在数组中添加转义字符
	 * 
	 * TODO 找时间将所有 nickName 的校验全部封装起来，供 Validattor 与 RegService 中重用，目前先只补下缺失的校验
	 * TODO RegService 中的 nickName 校验也要重用同一份代码，以免代码重复
	 */
	public static Ret validateNickName(String nickName) {
		if (StrKit.isBlank(nickName)) {
			return Ret.fail("msg", "昵称不能为空");
		}

		// 放开了 _-.  三个字符的限制
		String[] arr = {" ", "`", "~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "=", "+",
							"[", "]", "{", "}", "\\", "|", ";", ":", "'", "\"", ",", "<", ">", "/", "?",
							"　", "＠", "＃", "＆", "，", "。", "《", "》", "？" };   // 全角字符
		for (String s : arr) {
			if (nickName.contains(s)) {
				return Ret.fail("msg", "昵称不能包含字符: \"" + s +"\"");
			}
		}

		return Ret.ok();
	}
}
