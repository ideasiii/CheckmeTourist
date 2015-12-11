package com.openfile.checkmetourist;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginHandler extends BaseHandler
{

	private TextView	tvSkip					= null;
	private TextView	tvQQLogin				= null;
	private TextView	tvLogin					= null;
	private TextView	tvForgetPW				= null;
	private TextView	tvSignUp				= null;
	private TextView	tvMemberAddSkip			= null;
	private TextView	tvMemberAddOK			= null;
	
	//登入 帳密
	private EditText   etMemberAccount          = null;
	private EditText   etMemberPassword			= null;
	
	
	//註冊新會員
	private EditText	etName					= null;
	private EditText	etAccount				= null;
	private EditText	etPassword				= null;
	private EditText	etPasswordAgain			= null;
	private ImageView	ivWoman					= null;
	private ImageView	ivMan					= null;
	private FlipperView	flipperView				= null;
	private int			VIEW_ID_FORGET_PASSWORD	= 0;
	private int			VIEW_ID_MEMBER_ADD		= 0;
	private View		viewForgetPassword		= null;
	private View		viewMemberAdd			= null;
	
	//忘記密碼
	private TextView	tvForgetPWSkip			= null;
	private TextView	tvForgetPWSend			= null;
	private EditText    etSendAccount           = null;
	
	private int			bSex					= -1;	// 0:woman , 1:man

	public final static int MEMBER_LOGIN        = 1;
	public final static int MEMBER_REGISTER  	= 2;
	public final static int MEMBER_FORGET_PASSWD= 3;
	
	
	public LoginHandler(Activity activity, Handler handler)
	{
		super(activity, handler);
		if (null != activity)
		{
			activity.setContentView(R.layout.login);
		}
	}

	
	@Override
	protected void finalize() throws Throwable
	{
		flipperView = null;
		super.finalize();
	}

	public void init()
	{
		if (null == theActivity)
			return;
		tvLogin = (TextView) theActivity.findViewById(R.id.textViewBtnLogin);
		tvSkip = (TextView) theActivity.findViewById(R.id.textViewLoginSkip);
		tvQQLogin = (TextView) theActivity.findViewById(R.id.textViewQQLogin);
		tvForgetPW = (TextView) theActivity.findViewById(R.id.textViewForgetPW);
		tvSignUp = (TextView) theActivity.findViewById(R.id.textViewSignUp);
		flipperView = (FlipperView) theActivity.findViewById(R.id.flipperViewLoginView);
		
		etMemberAccount = (EditText) theActivity.findViewById(R.id.editTextEmail);
		etMemberPassword = (EditText) theActivity.findViewById(R.id.editTextPassword);
		
		
		
		if (null != tvSkip)
		{
			tvSkip.setOnTouchListener(itemTouchListener);
		}

		if (null != tvQQLogin)
		{
			tvQQLogin.setOnTouchListener(itemTouchListener);
		}

		if (null != tvLogin)
		{
			
			
			tvLogin.setOnTouchListener(itemTouchListener);
		}

		if (null != tvForgetPW)
		{
			tvForgetPW.setOnTouchListener(itemTouchListener);
		}

		if (null != tvSignUp)
		{
			tvSignUp.setOnTouchListener(itemTouchListener);
		}

		if (null != flipperView)
		{
			VIEW_ID_FORGET_PASSWORD = flipperView.addChild(R.layout.forget_password);
			VIEW_ID_MEMBER_ADD = flipperView.addChild(R.layout.member_add);
			viewForgetPassword = flipperView.getChildView(VIEW_ID_FORGET_PASSWORD);
			viewMemberAdd = flipperView.getChildView(VIEW_ID_MEMBER_ADD);

			if (null != viewForgetPassword)
			{
				tvForgetPWSkip = (TextView) viewForgetPassword.findViewById(R.id.textViewSendPasswordSkip);
				tvForgetPWSend = (TextView) viewForgetPassword.findViewById(R.id.textViewSendPassword);
				etSendAccount  = (EditText) theActivity.findViewById(R.id.editTextReqedEmail);
				if (null != tvForgetPWSkip)
				{
					tvForgetPWSkip.setOnTouchListener(itemTouchListener);
				}
				if (null != tvForgetPWSend)
				{
					tvForgetPWSend.setOnTouchListener(itemTouchListener);
				}
			}

			if (null != viewMemberAdd)
			{
				tvMemberAddSkip = (TextView) viewMemberAdd.findViewById(R.id.textViewMemberAddSkip);
				tvMemberAddOK = (TextView) viewMemberAdd.findViewById(R.id.textViewMemberAddOK);
				ivWoman = (ImageView) viewMemberAdd.findViewById(R.id.imageViewMemberAddWoman);
				ivMan = (ImageView) viewMemberAdd.findViewById(R.id.imageViewMemberAddMan);
				if (null != tvMemberAddSkip)
				{
					tvMemberAddSkip.setOnTouchListener(itemTouchListener);
				}

				if (null != tvMemberAddOK)
				{
					tvMemberAddOK.setOnTouchListener(itemTouchListener);
				}
				if (null != ivWoman)
				{
					ivWoman.setOnTouchListener(itemTouchListener);
				}
				if (null != ivMan)
				{
					ivMan.setOnTouchListener(itemTouchListener);
				}

				etName = (EditText) viewMemberAdd.findViewById(R.id.editTextMemberAddName);
				etAccount = (EditText) viewMemberAdd.findViewById(R.id.editTextMemberAddAccount);
				etPassword = (EditText) viewMemberAdd.findViewById(R.id.editTextMemberAddPassword);
				etPasswordAgain = (EditText) viewMemberAdd.findViewById(R.id.editTextMemberAddPasswordAgain);
			}
		}
	}
	
	public void flipperViewClose()
	{
		flipperView.close();
		
	}

	private void sexSelected(boolean bMan)
	{
		if (bMan)
		{
			bSex = 1;
			ivMan.setImageResource(R.drawable.man_sel);
			ivWoman.setImageResource(R.drawable.woman);
		}
		else
		{
			bSex = 0;
			ivWoman.setImageResource(R.drawable.woman_sel);
			ivMan.setImageResource(R.drawable.man);
		}
	}
	
	
	
	private boolean checkLoginInput()
	{
		boolean bCurrect = false;
		if(null != etMemberAccount && null != etMemberPassword)
		{
			if (0 >= etMemberAccount.length() )
			{
				DialogHandler.showAlert(theActivity, etMemberAccount.getHint().toString(), false);
			}
			else if ( !isEmailValid(etMemberAccount.getText().toString()))
			{
				DialogHandler.showAlert(theActivity, theActivity.getString(R.string.invalid_e_mail_format), false);
			}
			else if (0 >= etMemberPassword.length())
			{
				DialogHandler.showAlert(theActivity, etMemberPassword.getHint().toString(), false);
			}
			else{
				bCurrect =  true;
			}
				
		}
		return bCurrect;
		
		
	}

	private boolean isEmailValid(String email)
	{
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}
	private boolean checkForgetPasswdInput()
	{
		if (null == etSendAccount)
		{
			Logs.showTrace("etSendAccount is null");
			return false;
		} 
		else
		{
			if(!isEmailValid(etSendAccount.getText().toString()))
			{
				DialogHandler.showAlert(theActivity, theActivity.getString(R.string.invalid_e_mail_format), false);
				return false;
			}
		}
		
		return true;
	}

	private boolean checkRegistInput()
	{
		boolean bCurrect = false;

		if (null != etName && null != etAccount && null != etPassword && null != etPasswordAgain)
		{
			if (0 >= etName.length())
			{
				DialogHandler.showAlert(theActivity, etName.getHint().toString(), false);
			}
			else if (0 >= etAccount.length())
			{
				DialogHandler.showAlert(theActivity, etAccount.getHint().toString(), false);
			}
			else if ( !isEmailValid(etAccount.getText().toString()))
			{
				DialogHandler.showAlert(theActivity, theActivity.getString(R.string.invalid_e_mail_format), false);
			}
			else if (0 >= etPassword.length())
			{
				DialogHandler.showAlert(theActivity, etPassword.getHint().toString(), false);
			}
			
			else if (0 >= etPasswordAgain.length())
			{
				DialogHandler.showAlert(theActivity, etPasswordAgain.getHint().toString(), false);
			}
			else
			{
				if (!etPassword.getText().toString().trim().equals(etPasswordAgain.getText().toString().trim()))
				{
					DialogHandler.showAlert(theActivity, etPasswordAgain.getHint().toString(), false);
				}
				else if (-1 == bSex)
				{
					DialogHandler.showAlert(theActivity, theActivity.getString(R.string.setting_sex), false);
				}
				else if(etPassword.getText().toString().length()<6)
				{
					
					DialogHandler.showAlert(theActivity, theActivity.getString(R.string.password_length_error), false);
				}
				else
				{
					bCurrect = true;
				}
			}
		}
		return bCurrect;
	}
	
	/**
	 * nfag = 1 ===> login
	 * nfag = 2 ===> register
	 * 
	 * */
	private JSONObject memberDataToJsonString (int nfag)
	{
		JSONObject tmp = new JSONObject();
		Logs.showTrace(" in  memberDataToJsonString method "+ nfag );
		if(nfag ==MEMBER_LOGIN)
		{
			try{
			tmp.put("email", this.etMemberAccount.getText().toString());
			Global.mAndroidSerial = this.etMemberAccount.getText().toString();
			tmp.put("passwd", this.etMemberPassword.getText().toString());
			}
			catch(Exception e)
			{
				Logs.showTrace("some error in member(228) login where converting member to json type");
				
			}
			
		}
		else if(nfag ==MEMBER_REGISTER)
		{
			
			try
			{
				tmp.put("email", this.etAccount.getText().toString());
				Global.mAndroidSerial = this.etAccount.getText().toString();
				tmp.put("passwd", this.etPassword.getText().toString());
				tmp.put("nickname", this.etName.getText().toString());
				if (this.bSex == 1)
				{
					tmp.put("gender", "man");
				} else if (this.bSex == 0)
				{
					tmp.put("gender", "woman");
				}
			
			
			} catch (JSONException e)
			{
				// TODO Auto-generated catch block
				Logs.showTrace("some error in member(254) append where converting member to json type");
			}
			
			
		}
		else if(nfag == MEMBER_FORGET_PASSWD)
		{
			try
			{
				Logs.showTrace("e-mail is :" + etSendAccount.getText().toString());
				tmp.put("email",etSendAccount.getText().toString());
				
			} catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		return tmp;
		
	}

	private OnTouchListener	itemTouchListener	= new OnTouchListener()
												{
													@Override
													public boolean onTouch(View v, MotionEvent event)
													{
														if (MotionEvent.ACTION_DOWN == event.getAction())
														{
															int nResId = v.getId();
															if (v instanceof TextView)
															{
																switch (nResId)
																{
																	case R.id.textViewLoginSkip:
																		postMsg(MSG.LOGIN_SKIP, 0, 0, null);
																		break;
																	case R.id.textViewBtnLogin:
																		if(checkLoginInput())
																		{
																			Logs.showTrace("Call Member Login API");
																			postMsg(MSG.MEMBER_LOGIN, 0, 0, memberDataToJsonString(MEMBER_LOGIN));
																		}
																		break;
																	case R.id.textViewQQLogin:
																		
																		postMsg(MSG.QQ_LOGIN, 0, 0, null);
																		break;
																	case R.id.textViewForgetPW:
																		flipperView.showView(VIEW_ID_FORGET_PASSWORD);
																		break;
																	case R.id.textViewSendPasswordSkip:
																	case R.id.textViewMemberAddSkip:
																		flipperView.close();
																		break;
																	case R.id.textViewSendPassword:
																		if(checkForgetPasswdInput())
																		{
																			Logs.showTrace("Call Member Forgot Password API");
																			postMsg(MSG.MEMBER_FORGET_PASSWD ,0, 0,memberDataToJsonString(MEMBER_FORGET_PASSWD));
																			
																		}
																		else 
																		{
																			
																		}
																		break;			
																	case R.id.textViewSignUp:
																		flipperView.showView(VIEW_ID_MEMBER_ADD);
																		break;
																	case R.id.textViewMemberAddOK:
																		if (checkRegistInput())
																		{
																			Logs.showTrace("Call Member Add API");
																			postMsg(MSG.MEMBER_REGISTER, 0, 0, memberDataToJsonString(MEMBER_REGISTER));
																		}
																		break;
																}
															}

															if (v instanceof ImageView)
															{
																switch (nResId)
																{
																	case R.id.imageViewMemberAddWoman:
																		sexSelected(false);
																		break;
																	case R.id.imageViewMemberAddMan:
																		sexSelected(true);
																		break;
																}
															}
														}
														return true;
													}
												};
}
