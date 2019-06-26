package member.signup;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import encryption.Encryption;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import member.mailingSystem.Mail;
import member.service.IMemberService;
import member.service.MemberServiceImpl;
import regEx.RegEx;
import vo.MemberVO;

public class SignUpController implements Initializable{
	
	IMemberService service = MemberServiceImpl.getInstance();
	
	
	@FXML
	private JFXButton btnClose;
	
	@FXML
	private JFXTextField tfMemId;
	
	@FXML
	private JFXButton btnDuplCheck;
	
	@FXML
	private JFXTextField tfMemName;
	
	@FXML
	private JFXTextField tfMail1;
	
	@FXML
	private JFXComboBox<String> cbMail2;
	
	@FXML
	private JFXButton btnReqIdNum;
	
	@FXML
	private JFXTextField tfIdNum;

	@FXML
    private JFXButton btnConfIdNum;
	
	@FXML
	private JFXTextField tfMemPw;
	
	@FXML
	private JFXTextField tfConfMemPw;
	
	@FXML
	private JFXComboBox<String> cbMemPhone1;
	
	@FXML
	private JFXTextField tfMemPhone2;
	
	@FXML
	private JFXTextField tfMemPhone3;
	
	@FXML
	private JFXTextField tfMemAddr1;
	
	@FXML
	private JFXButton btnSearchAddr;
	
	@FXML
	private JFXTextField tfMemAddr2;
	
	@FXML
	private DatePicker dpMemBirth;

    @FXML
    private JFXComboBox<String> cbMemAuth;
    
    @FXML
    private JFXButton btnSignUp;
    
    int idChk=0;
    int emailChk=0;
    int addrChk=0;
    int emailIdNum;
    Date birth = null;
    
    @FXML
    void onCloseClick(ActionEvent event) {
    	((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    @FXML
    void checkDuplication(ActionEvent event) {
    	
    	String id = tfMemId.getText();
    	if(id.trim().isEmpty()) {
    		Alert error = new Alert(AlertType.ERROR);
    		error.setTitle("ERROR");
    		error.setContentText("아이디를 입력해주세요");
    		error.showAndWait();
    		return;
    	}
    	
    	
    	IMemberService service = MemberServiceImpl.getInstance();
    	int chk = service.checkDuplication(id);
    	
    	if(chk>0) {
    		Alert error = new Alert(AlertType.ERROR);
    		error.setTitle("ERROR");
    		error.setContentText("동일한 아이디가 존재합니다. 다시 입력해주세요.");
    		error.showAndWait();
    		tfMemId.setText(null);
    		idChk=0;
    	}else {
    		Alert conf = new Alert(AlertType.CONFIRMATION);
    		conf.setTitle("CONFIRMATION");
    		conf.setContentText("사용할 수 있는 아이디입니다. 사용하시겠습니까?");
    		
    		ButtonType btnResult = conf.showAndWait().get();
    		if(btnResult==ButtonType.OK) {
    			tfMemId.setText(id);
    			idChk=1;
    		}else {
    			tfMemId.setText(null);
    			idChk=0;
    		}
    	}
    }
    
    @FXML
    void requestIdNum(ActionEvent event) {
    	String mail1 = tfMail1.getText().trim();
    	String mail2 = (String) cbMail2.getSelectionModel().getSelectedItem();
    	String email = mail1+"@"+mail2;
    	
    	emailIdNum = (int)(Math.random()*8999+1000);
    	
    	String from = "ddhrdmakeit@gmail.com"; // 보내는 사람의 이메일
		String to = email; // 받는 사람의 이메일
		String cc = ""; //참조하는 이메일
		String subject = "니방내방에 오신것을 환영합니다.";
		String content = (String)("귀하의 인증 Email 인증번호는"+"\n"+emailIdNum+"\n"+"입니다.");
		
		if(from.trim().equals("")) {
			System.out.println("보내는 사람을 입력하지 않으셨습니다.");
		}else if(to.trim().equals("")) {
			System.out.println("받는 사람을 입력하지 않으셨습니다.");
		}else {
			System.out.println("메일 전송 중....");
		
			try {
				Mail mt = new Mail();
				mt.sendMail(from, to, cc, subject, content);
				System.out.println("인증 메일을 전송하였습니다.");
				tfIdNum.setDisable(false);
				btnConfIdNum.setDisable(false);
			} catch (Exception e) {
				System.out.println("메일 전송에 실패하였습니다.");
				System.out.println("실패 이유 : "+e.getMessage());
			}
		}
    }
    
    @FXML
    void confirmIdNum(ActionEvent event) {
    	String idNum = tfIdNum.getText().trim();
    	if(RegEx.checkInt(idNum) == false) {
    		Alert alert = new Alert(AlertType.INFORMATION);
        	alert.setTitle("니방내방");
        	alert.setHeaderText("Oops!");
        	alert.setContentText("숫자만 입력해주세요");
        	alert.showAndWait();
        	return;
    	}
    	
    	int confIdNum=Integer.parseInt(idNum);
    	if(confIdNum==emailIdNum) {
    		Alert info = new Alert(AlertType.INFORMATION);
    		info.setTitle("이메일 인증 완료");
    		info.setContentText("이메일 인증에 성공하셨습니다.");
    		info.showAndWait();
    		tfIdNum.setText("");
    		tfIdNum.setDisable(true);
    		emailChk=1;
    	}else {
    		Alert error = new Alert(AlertType.ERROR);
    		error.setTitle("이메일 인증 실패");
    		error.setContentText("이메일 인증에 실패하셨습니다.");
    		error.showAndWait();
    		emailChk=0;
    	}
    }
    
    /**
     * 주소찾기
     * @param event
     */
    @FXML
    void searchAddress(ActionEvent event) {
    	String inputAddr = tfMemAddr1.getText().trim();
    	if(inputAddr.isEmpty()) {
    		Alert error = new Alert(AlertType.INFORMATION);
    		error.setTitle("니방내방");
    		error.setContentText("주소를 입력해주세요");
    		error.showAndWait();
    		return;
    	}
    	String address = service.getAddress(inputAddr);
    	if(address.isEmpty()) {
    		Alert error = new Alert(AlertType.INFORMATION);
    		error.setTitle("니방내방");
    		error.setContentText("잘못된 주소를 입력하셨습니다");
    		error.showAndWait();
    		return;
    	}
    	tfMemAddr2.setText(address);
    	addrChk=1;
    }
    
    @FXML
    void onDatePicked(ActionEvent event) {
    	LocalDate date =  dpMemBirth.getValue();
    	Date sqlDate = Date.valueOf(date);
    	birth = sqlDate;
    }
    
    @FXML
    void signUp(ActionEvent event) {
    	String id = tfMemId.getText().trim();
    	String name = tfMemName.getText().trim();
    	String mail1 = tfMail1.getText().trim();
    	String mail2 = (String) cbMail2.getSelectionModel().getSelectedItem();
    	String email = mail1+"@"+mail2;
    	String pw = tfMemPw.getText().trim();
    	String pw2 = tfConfMemPw.getText().toString().trim();
    	String phone1 = (String) cbMemPhone1.getSelectionModel().getSelectedItem(); 
    	String phone2 = tfMemPhone2.getText().trim();
    	String phone3 = tfMemPhone3.getText().trim();
    	String phoneNum = phone1+"-"+phone2+"-"+phone3;
    	String addr1 = tfMemAddr1.getText().trim();
    	String addr2 = tfMemAddr2.getText().trim();
    	if(cbMemAuth.getSelectionModel().getSelectedItem() == null) {
    		Alert alert = new Alert(AlertType.INFORMATION);
        	alert.setTitle("니방내방");
        	alert.setHeaderText("Oops!");
        	alert.setContentText("입력하지 않은 항목이 있습니다.");
        	alert.showAndWait();
    		return;
    	}
    	String reason = (String) cbMemAuth.getSelectionModel().getSelectedItem();
    	String auth=null;
    	
    	if (reason.equals("새로운 방을 찾고있어요!")) {
    		auth="임차인";
    	}else if(reason.equals("빈 방을 내놓고 싶어요!")) {
    		auth="임대인";
    	}else {
    		auth="공인중개사";
    	}
    	
    	IMemberService service = MemberServiceImpl.getInstance();
    	
    	if(id.isEmpty()) {
    		Alert error = new Alert(AlertType.ERROR);
    		error.setTitle("회원가입 실패");
    		error.setContentText("아이디를 입력해주세요");
    		error.showAndWait();
    	}else if(idChk==0) {
    		Alert error = new Alert(AlertType.ERROR);
    		error.setTitle("회원가입 실패");
    		error.setContentText("아이디 중복 여부를 확인해주세요.");
    		error.showAndWait();
    	}else if(mail1.isEmpty()) {
    		Alert error = new Alert(AlertType.ERROR);
    		error.setTitle("회원가입 실패");
    		error.setContentText("이메일 주소를 입력해주세요");
    		error.showAndWait();
    	}else if(emailChk==0) {
    		Alert error = new Alert(AlertType.ERROR);
    		error.setTitle("회원가입 실패");
    		error.setContentText("이메일 인증을 해주세요.");
    		error.showAndWait();
    	}else if(pw.isEmpty()) {
    		Alert error = new Alert(AlertType.ERROR);
    		error.setTitle("회원가입 실패");
    		error.setContentText("비밀번호를 입력해주세요");
    		error.showAndWait();
    	}else if(!pw.equals(pw2)) {
    		Alert error = new Alert(AlertType.ERROR);
    		error.setTitle("회원가입 실패");
    		error.setContentText("비밀번호와 비밀번호 확인이 다릅니다. 다시 입력해주세요.");
    		error.showAndWait();
    		pw=null;
    		pw2=null;
    	}else if(phone1.isEmpty()||phone2.trim().isEmpty()||phone3.trim().isEmpty()) {
    		Alert error = new Alert(AlertType.ERROR);
    		error.setTitle("회원가입 실패");
    		error.setContentText("핸드폰 번호를 입력해주세요.");
    		error.showAndWait();
    	}else if(addr1.isEmpty()||addr2.trim().isEmpty()) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("회원가입 실패");
			error.setContentText("주소를 입력해주세요.");
			error.showAndWait();
    	}else if(addrChk==0) {
    		Alert error = new Alert(AlertType.ERROR);
    		error.setTitle("회원가입 실패");
    		error.setContentText("주소를 확인주세요.");
    		error.showAndWait();
    	}else if(birth==null) {
    		Alert error = new Alert(AlertType.ERROR);
    		error.setTitle("회원가입 실패");
    		error.setContentText("생일을 입력해주세요.");
    		error.showAndWait();
    	}else if(auth.isEmpty()) {
    		Alert error = new Alert(AlertType.ERROR);
    		error.setTitle("회원가입 실패");
    		error.setContentText("회원가입 목적을 입력해주세요.");
    		error.showAndWait();
    	}
    	else {
    		
    		MemberVO mv = new MemberVO(id, pw, name, birth, email, phoneNum, addr1, addr2, auth);
    		int cnt = service.signUp(mv);
    		if (cnt>0) {
    			Alert info = new Alert(AlertType.INFORMATION);
    			info.setTitle("회원가입 성공");
    			info.setContentText("회원가입에 성공하셨습니다.");
    			info.showAndWait();
    			//창닫기
    			((Node)(event.getSource())).getScene().getWindow().hide();
    		}else {
    			Alert error = new Alert(AlertType.ERROR);
    			error.setTitle("회원가입 실패");
        		error.setContentText("다시 시도해주세요.");
        		error.showAndWait();
    		}
    	}
    }
	
    /**
     * 암호화 
     * 비밀번호 비교
     */
    public static boolean checkPassword(String mem_pw, String mem_checkpw) {
    	Encryption securityUtil = new Encryption();
		
		String rtn1 = securityUtil.encryptSHA256(mem_pw);
		String rtn2 = securityUtil.encryptSHA256(mem_checkpw);
		if(rtn1.equals(rtn2)) {
		return true;
		}
		return false;
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		ObservableList<String> domainList = 
				FXCollections.observableArrayList("naver.com", "daum.net", "gmail.com", "hotmail.com", "hanmail.net", "nate.com", "yahoo.co.kr");
		ObservableList<String> phoneIdNumList = 
				FXCollections.observableArrayList("010", "011", "016", "017", "018", "019");
		ObservableList<String> authorityList = 
				FXCollections.observableArrayList("새로운 방을 찾고있어요!", "빈 방을 내놓고 싶어요!", "임차인을 구하는 중이에요!");
		
		cbMail2.setItems(domainList);
		cbMemPhone1.setItems(phoneIdNumList);
		cbMemAuth.setItems(authorityList);
		
		
	}
	
	
}
