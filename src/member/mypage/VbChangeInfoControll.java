package member.mypage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.view.BuildedSqlMapConfig;
import main.view.MainViewController;
import member.dao.IMemberDaoImpl;
import member.service.IMemberService;
import member.service.MemberServiceImpl;
import regEx.RegEx;
import vo.MemberVO;

public class VbChangeInfoControll implements Initializable{
	
	MemberVO currentMember = MainViewController.getCurrentMember();
	SqlMapClient smc = BuildedSqlMapConfig.getInstance();

    @FXML
    private JFXComboBox<String> CbPhone1;

    @FXML
    private JFXButton btnPhone;

    @FXML
    private JFXTextField tfPhone3;

    @FXML
    private VBox vbChangeInfo;

    @FXML
    private JFXTextField tfPhone2;

    @FXML
    private JFXComboBox<String> cbMemberType;

    @FXML
    private JFXTextField tfCurrentPw;

    @FXML
    private JFXTextField tfUpdatePw2;

    @FXML
    private JFXTextField tfUpdatePw;

    @FXML
    private JFXTextField tfEmail;

    @FXML
    private JFXTextField tfMemberName;

    @FXML
    private JFXButton btnDelAccount;

    @FXML
    void onPhoneClick(ActionEvent event) {
    	String phoneStr = CbPhone1.getSelectionModel().getSelectedItem()+"-"
    			+ tfPhone2.getText() + "-" + tfPhone3.getText();
    	System.out.println(phoneStr);
    	if(!RegEx.checkPhone(phoneStr)) {
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("니방내방");
    		alert.setHeaderText("Oops");
    		alert.setContentText("핸드폰 번호가 잘못되었어요!");
    		alert.showAndWait();
    		return;
    	}
    	currentMember.setMem_phone(phoneStr);
    	try {
			if(smc.update("member.changePhone",currentMember) == 0) {
				System.out.println("아무것도 바뀐게 없음");
			}else {
				Alert alert = new Alert(AlertType.INFORMATION);
	    		alert.setTitle("니방내방");
	    		alert.setHeaderText("Success");
	    		alert.setContentText("핸드폰 번호가 변경되었습니다.");
	    		alert.showAndWait();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("아 번호 바꾸기 실패함 ㅠㅠ");
		}
    	
    }



    /**
     * 취소 버튼 클릭시
     * @param event
     */
    @FXML
    void onCancelClick(ActionEvent event) {
    	MainViewController.getInstance().setDefaultMain();
    }

    /**
     * 비밀번호 변경 클릭시
     * @param event
     */
    @FXML
    void onCommitClick(ActionEvent event) {
    	String currentPassword = tfCurrentPw.getText();
    	String changePassword = tfUpdatePw.getText();
    	String ckPassword = tfUpdatePw2.getText();
    	if(currentPassword.trim().isEmpty()) {
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("니방내방");
    		alert.setHeaderText("Oops");
    		alert.setContentText("현재 비밀번호를 입력해주세요");
    		alert.showAndWait();
    		return;
    	}else if(changePassword.trim().isEmpty()) {
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("니방내방");
    		alert.setHeaderText("Oops");
    		alert.setContentText("변경할 비밀번호를 입력해주세요");
    		alert.showAndWait();
    		return;
    	}else if(ckPassword.trim().isEmpty()) {
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("니방내방");
    		alert.setHeaderText("Oops");
    		alert.setContentText("두번째 변경할 비밀번호를 입력해주세요");
    		alert.showAndWait();
    		return;
    	}
    	if(!currentPassword.equals(currentMember.getMem_pw())) {
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("니방내방");
    		alert.setHeaderText("Oops");
    		alert.setContentText("현재 비밀번호를 잘못 입력하셨습니다");
    		alert.showAndWait();
    		return;
    	}
    	if(!changePassword.equals(ckPassword)) {
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("니방내방");
    		alert.setHeaderText("Oops");
    		alert.setContentText("입력된 변경할 비밀번호가 서로 다릅니다");
    		alert.showAndWait();
    		return;
    	}
    	currentMember.setMem_pw(ckPassword);
    	try {
			smc.update("member.changePw", currentMember);
		} catch (SQLException e) {
			System.out.println("패스워드 업뎃 실패함 ㅡㅡ");
		}
    	
    	Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("니방내방");
		alert.setHeaderText("Success");
		alert.setContentText("비밀번호가 변경되었습니다!");
		alert.showAndWait();
    	
    }
    
    @FXML
    void onDelAccount(ActionEvent event) {
    	
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("member/deleteAccount/DeleteAccount.fxml"));
			Parent delAccNodes = loader.load();
			Stage stage = new Stage();
			Scene scene = new Scene(delAccNodes);
			stage.setScene(scene);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	ObservableList<String> phoneList = FXCollections.observableArrayList();
		ObservableList<String> authList = FXCollections.observableArrayList();
		phoneList.addAll("010", "011", "016", "017", "018", "019");
		authList.addAll("admin", "owner", "default");
		CbPhone1.setItems(phoneList);
		cbMemberType.setItems(authList);
		cbMemberType.getSelectionModel().select(authList.indexOf(currentMember.getMem_auth()));
		cbMemberType.setEditable(false);
		cbMemberType.setDisable(true);
		tfEmail.setText(currentMember.getMem_mail());
		tfMemberName.setText(currentMember.getMem_name());
		String[] phone = currentMember.getMem_phone().split("-");
		CbPhone1.getSelectionModel().select(phoneList.indexOf(phone[0]));
		tfPhone2.setText(phone[1]);
		tfPhone3.setText(phone[2]);
    }


}
