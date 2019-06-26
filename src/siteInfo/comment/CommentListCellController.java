package siteInfo.comment;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.view.MainViewController;
import siteInfo.articlePage.ArticlePageController;
import siteInfo.service.ISiteInfoService;
import siteInfo.service.ISiteInfoServiceImpl;
import siteInfo.siteInfo.SiteInfoController;
import vo.BoardVO;
import vo.CommentVO;
import vo.MemberVO;

public class CommentListCellController implements Initializable {
	
	private MemberVO currentMemberVO= MainViewController.getCurrentMember();
	private BoardVO currentBoardVO= ArticlePageController.getCurrentBoardVO();
	ArticlePageController apc = ArticlePageController.getInstance();
	SiteInfoController siteInfoController;
	ObservableList<BoardVO> list;
	
	private CommentVO currentCommentVO;
	
	public CommentVO getCurrentCommentVO() {
		return currentCommentVO;
	}
	
	public void setCurrentCommentVO(CommentVO currentCommentVO) {
		this.currentCommentVO = currentCommentVO;
	}
	
    @FXML
    private HBox boardCell;

    @FXML
    private Label lbName;

    @FXML
    private JFXTextField tfContent;

    @FXML
    private Label lbDate;

    @FXML
    private JFXButton btnCmt;
    
    @FXML
    private JFXButton btnEdit;

    @FXML
    private JFXButton btnDel;
    
    @FXML
    private VBox vbCmt;
	
    @FXML
    void onBtnCmt(ActionEvent event) {
    	ISiteInfoService service = ISiteInfoServiceImpl.getInstance();
    	String updatedContent = tfContent.getText().toString();
    	currentCommentVO.setComment_content(updatedContent);
    	int cnt = service.updateComment(currentCommentVO);
    	if(cnt>0) {
    		Alert info = new Alert(AlertType.INFORMATION);
    		info.setTitle("댓글 수정 성공");
    		info.setContentText("댓글을 성공적으로 수정하였습니다.");
    		info.showAndWait();
    		
			try {
				apc.getVbCommentBox().getChildren().clear();
				FXMLLoader commentBoxLoader = new FXMLLoader(getClass().getClassLoader().getResource("siteinfo/comment/CommentBox.fxml"));
				Parent commentBoxNodes = commentBoxLoader.load();
				apc.getVbCommentBox().getChildren().add(commentBoxNodes);
			}catch (Exception e) {
				e.printStackTrace();
			}
		
    	}else {
    		Alert error = new Alert(AlertType.ERROR);
    		error.setTitle("댓글 수정 실패");
    		error.setContentText("댓글 수정이 실패하였습니다.");
    		error.showAndWait();
    	}
    }
    
	@FXML
    void onBtnEdit(ActionEvent event) {
		tfContent.setEditable(true);
		btnCmt.setDisable(false);
		btnCmt.setVisible(true);
		tfContent.setMouseTransparent(false);
		tfContent.setFocusTraversable(true);
		tfContent.requestFocus();
    }

    @FXML
    void onBtnDel(ActionEvent event) {
    	ISiteInfoService service = ISiteInfoServiceImpl.getInstance();
    	int cnt = service.deleteComment(currentCommentVO);
    	if(cnt>0) {
    		Alert info = new Alert(AlertType.INFORMATION);
			info.setTitle("댓글 삭제");
			info.setContentText("댓글을 성공적으로 삭제하였습니다.");
			info.showAndWait();
			
			try {
				apc.getVbCommentBox().getChildren().clear();
				FXMLLoader commentBoxLoader = new FXMLLoader(getClass().getClassLoader().getResource("siteinfo/comment/CommentBox.fxml"));
				Parent commentBoxNodes = commentBoxLoader.load();
				apc.getVbCommentBox().getChildren().add(commentBoxNodes);
			}catch (Exception e) {
				e.printStackTrace();
			}
			
    	}else {
    		Alert error = new Alert(AlertType.ERROR);
			error.setTitle("댓글 삭제");
			error.setContentText("댓글 삭제에 실패하였습니다.");
			error.showAndWait();
    	}
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		boardCell.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				
				if(currentMemberVO == null) {
					return;
				}
				
				if(currentMemberVO.getMem_id().equals(lbName.getText())) {
					btnEdit.setDisable(false);
					btnEdit.setVisible(true);
					btnDel.setDisable(false);
					btnDel.setVisible(true);
				}
			}
		});
	}
	
	public void setItem(CommentVO cv) {
		this.currentCommentVO = cv;
		
		lbName.setText(cv.getMem_id());
		tfContent.setText(cv.getComment_content());
		lbDate.setText(cv.getComment_date().toString());
		
	}
}
