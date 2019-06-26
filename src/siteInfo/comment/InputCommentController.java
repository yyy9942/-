package siteInfo.comment;

import java.net.URL;
import java.sql.Date;
import java.util.Calendar;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import main.view.MainViewController;
import siteInfo.articlePage.ArticlePageController;
import siteInfo.service.ISiteInfoService;
import siteInfo.service.ISiteInfoServiceImpl;
import vo.BoardVO;
import vo.CommentVO;
import vo.MemberVO;

public class InputCommentController implements Initializable{
	
	MemberVO currentMember = MainViewController.getCurrentMember();
	ISiteInfoService service = ISiteInfoServiceImpl.getInstance();
	ArticlePageController apc = ArticlePageController.getInstance();
	private BoardVO currentBoardVO = ArticlePageController.getCurrentBoardVO();
	
	public BoardVO getCurrentBoardVO() {
		return currentBoardVO;
	}

	public void setCurrentBoardVO(BoardVO currentBoardVO) {
		this.currentBoardVO = currentBoardVO;
	}
	
	@FXML
    private JFXTextArea taCommentContent;

    @FXML
    private HBox hbCommentInputBox;

    @FXML
    private JFXButton btnAddComment;

    @FXML
    void onAddComment(ActionEvent event) {
    	if(taCommentContent.getText()!=null) {
    		CommentVO cv = new CommentVO();
    		cv.setBoard_id(currentBoardVO.getBoard_id());
    		cv.setMem_id(currentMember.getMem_id());
    		cv.setComment_content(taCommentContent.getText());
    		cv.setComment_date(new Date(Calendar.getInstance().getTime().getTime()));
    		
    		int cnt = service.addComment(cv);
    		if(cnt==1) {
    			try {
    				apc.getVbCommentBox().getChildren().clear();
    				FXMLLoader commentBoxLoader = new FXMLLoader(getClass().getClassLoader().getResource("siteinfo/comment/CommentBox.fxml"));
    				Parent commentBoxNodes = commentBoxLoader.load();
    				apc.getVbCommentBox().getChildren().add(commentBoxNodes);
    			}catch (Exception e) {
    				e.printStackTrace();
				}
    		}
    		
    	}else {
    		Alert error = new Alert(AlertType.ERROR);
    		error.setTitle("댓글 등록 실패");
    		error.setContentText("댓글 내용이 없습니다.");
    		error.showAndWait();
    	}
    }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

}
