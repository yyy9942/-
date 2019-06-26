package member.login;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.jfoenix.controls.JFXButton;

import encryption.Encryption;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import main.view.BuildedSqlMapConfig;
import main.view.MainViewController;
import member.service.MemberServiceImpl;
import owner.main.OwnerMainController;
import vo.BlacklistVO;
import vo.MemberVO;

public class LogInController implements Initializable{
	
	SqlMapClient smc = null;
	{
		smc = BuildedSqlMapConfig.getInstance();
	}
	
	private static LogInController logInController;
	
	public LogInController getInstance() {
		return logInController;
	}
	
	@FXML
    private VBox vbLogIn;
    
	@FXML
    private JFXButton btnLogin;

    @FXML
    private JFXButton btnFindPassword;

    @FXML
    private JFXButton btnLoginByKakao;

    @FXML
    private JFXButton BtnSignin;

    @FXML
    private TextField tfId;

    @FXML
    private JFXButton btnGotoRealtor;

    @FXML
    private PasswordField tfPw;

    @FXML
    private JFXButton btnGotoOwner;
    
    /**
     * 비밀번호 찾기 클릭 이벤트
     * @param event
     */
    @FXML
    void onFindPasswordClick(ActionEvent event) {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("member/login/FindPW.fxml"));
			Parent findPWNodes = loader.load();
			vbLogIn.getChildren().clear();
			vbLogIn.getChildren().add(findPWNodes);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 닫기 클릭시
     * @param event
     */
    @FXML
    void onCloseClick(ActionEvent event) {
    	((Node)(event.getSource())).getScene().getWindow().hide();
    }

    /**
     * 로그인 버튼 클릭시
     * 아이디, 패스워드가 공백인지 검사한 후
     * 데이터베이스와 연동하여 검사한다.
     * @param event
     */
    @FXML
    void onLoginClick(ActionEvent event) {
    	String id = tfId.getText();
    	String pw = tfPw.getText();
    	Map<String, String> param = new HashMap<>();
    	MemberVO currentMember = null;
    	if(id.isEmpty()) {
    		Alert idError = new Alert(AlertType.INFORMATION);
    		idError.setTitle("니방내방");
    		idError.setContentText("아이디를 먼저 입력해주세요");
    		idError.showAndWait();
    		return;
    	}
    	
    	if(pw.isEmpty()) {
    		Alert idError = new Alert(AlertType.INFORMATION);
    		idError.setTitle("니방내방");
    		idError.setContentText("패스워드를 먼저 입력해주세요");
    		idError.showAndWait();
    		return;
    	}
    	// 블랙리스트에 등재되어있는지 확인한다.
    	List<BlacklistVO> blList = MemberServiceImpl.getInstance().checkBlacklist(id);
    	if(blList.size() != 0) {
    		// 블랙리스트로 조회되었을 때
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("니방내방");
    		alert.setHeaderText("다음과 같은 사유로 니방내방을 이용하실 수 없습니다.");
    		alert.setContentText("사유 : " + blList.get(0).getBlacklist_comment());
    		alert.showAndWait();
    		// 로그인을 종료한다.
    		((Node)(event.getSource())).getScene().getWindow().hide();
    		return;
    	}
    	
    	
    	param.put("mem_id", id);
    	String shaPass = Encryption.encryptSHA256(pw);
    	param.put("mem_pw", shaPass);
    	try {
    		if(shaPass!=null) {
			currentMember = (MemberVO) smc.queryForObject("member.logIn",param);
				if(OwnerMainController.getInstance() != null) {
					OwnerMainController.getInstance().onLogin();
				}
    		} else {
    			return;
    		}
		} catch (SQLException e) {
			System.out.println("아 실패함");
			e.printStackTrace();
		}
    	
    	// 로그인 정보가 틀리면 currentMember에 null이 들어온다
    	if(currentMember == null) {
    		Alert failLogin = new Alert(AlertType.INFORMATION);
    		failLogin.setTitle("니방내방");
    		failLogin.setHeaderText("Oops!");
    		failLogin.setContentText("로그인 정보가 잘못되었습니다!\n다시 입력해주세요");
    		failLogin.showAndWait();
    	}else {
    		MainViewController.getInstance().onLogIn(currentMember);
    		((Node)(event.getSource())).getScene().getWindow().hide();
    	}
    	
	
    }


    /**
     * 회원가입 클릭시
     * @param event
     */
    @FXML
    void onSigninClick(ActionEvent event) {
    	MainViewController.getInstance().onRegisterClick(event);
    	((Node)(event.getSource())).getScene().getWindow().hide();
    }

    

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
		
		
		
		
	}
}
