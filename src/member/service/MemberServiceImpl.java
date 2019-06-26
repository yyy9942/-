package member.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import encryption.Encryption;
import javafx.collections.ObservableList;
import member.dao.IMemberDao;
import member.dao.IMemberDaoImpl;
import vo.BlacklistVO;
import vo.BoardVO;
import vo.MemberVO;
import vo.ReviewVO;

public class MemberServiceImpl implements IMemberService{

	private static MemberServiceImpl service;
	private IMemberDao dao;
	
	private MemberServiceImpl() {
		dao=IMemberDaoImpl.getInstance();
	}
	
	public static IMemberService getInstance() {
		if(service==null) {
			service = new MemberServiceImpl();
		}
		return service;
	}

	@Override
	public int checkDuplication(String id) {
		int chk = 0;
		try {
			chk = dao.checkDuplication(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return chk;
	}

	@Override
	public int signUp(MemberVO mv) {
		int cnt = 0;
		try {
			// 암호화
			String shaPass = Encryption.encryptSHA256(mv.getMem_pw());
			mv.setMem_pw(shaPass);
			
			cnt = dao.signUp(mv);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cnt;
	}
	
	@Override
	public String getAddress(String searchStr) {
		return dao.getAddress(searchStr);
	}
	
	
	@Override
	public ObservableList<BoardVO> getMyBoardList(String mem_id) {
		return dao.getMyBoardList(mem_id);
	}
	
	@Override
	public List<Map<String, Object>> getReceiveContrace(String mem_id) {
		return dao.getReceiveContrace(mem_id);
	}
	
	
	@Override
	public void newContract(Map<String, Object> param) {
		dao.newContract(param);
	}

	@Override
	public List<Map<String, Object>> getContract(String mem_id) {
		return dao.getContract(mem_id);
	}

	@Override
	public void insertReview(ReviewVO rv) {
		dao.insertReview(rv);
	}
	
	@Override
	public List<ReviewVO> getMyReview(String mem_id) {
		return dao.getMyReview(mem_id);
	}
	
	@Override
	public void deleteReview(int review_id) {
		dao.deleteReview(review_id);
	}
	
	@Override
	public void updateReview(ReviewVO rv) {
		dao.updateReview(rv);
	}
	
	@Override
	public List<BlacklistVO> checkBlacklist(String mem_id) {
		return dao.checkBlacklist(mem_id);
	}
	
	@Override
	public void deleteContract(int room_id) {
		dao.deleteContract(room_id);
	}

	@Override
	public int deleteAccount(MemberVO currentMember) {
		int cnt=0;
		try {
			cnt = dao.deleteAccount(currentMember);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cnt;
	}

	@Override
	public int checkCorrectEmail(Map<String, String> param1) {
		int chk=0;
		try {
			chk=dao.checkCorrectEmail(param1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return chk;
	}

	@Override
	public int updateTempPw(Map<String, String> param2) {
		int cnt=0;
		try {
			cnt = dao.updateTempPw(param2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cnt;
	}
}
