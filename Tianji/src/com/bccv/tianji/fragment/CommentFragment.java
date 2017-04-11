package com.bccv.tianji.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bccv.tianji.R;
import com.bccv.tianji.activity.dialog.ReplyDialog;
import com.bccv.tianji.activity.dialog.LogInDialog.Dialogcallback;
import com.bccv.tianji.activity.dialog.ReplyDialog.ReplayDialogcallback;
import com.bccv.tianji.adapter.CommentAdapter;
import com.bccv.tianji.adapter.CommentAdapter.Completecallback;
import com.bccv.tianji.api.CommentApi;
import com.bccv.tianji.api.GameApi;
import com.bccv.tianji.model.CommentReply;
import com.bccv.tianji.model.Gameinfo;
import com.utils.net.NetUtil;
import com.utils.pulltorefresh.FooterLoadingLayout;
import com.utils.pulltorefresh.PullToRefreshBase;
import com.utils.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.utils.pulltorefresh.PullToRefreshListView;
import com.utils.tools.GlobalParams;
import com.utils.tools.PromptManager;

@SuppressLint("NewApi")
public class CommentFragment extends Fragment {

	private TextView AllScoreText;
	private TextView CommentNum;
	private ProgressBar bar1, bar2, bar3, bar4, bar5;
	private TextView Score1, Score2, Score3, Score4, Score5;
	private int page = 1, count = 10;
	private String GameId;
	private CommentAdapter adapter;
	private Boolean isFresh;
	private List<CommentReply> data, getList;

	private Gameinfo gameInfo;
	private String sart1, sart2, sart3, sart4, sart5, totalStar, Point;

	private PullToRefreshListView pullToRefreshListView;
	private Button CommentBtn;
	private String userID="";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_comment, null);

		GameId = getGameId();

		AllScoreText = (TextView) view.findViewById(R.id.comment_allSoreText);
		bar1 = (ProgressBar) view.findViewById(R.id.comment_ScoreBar1);
		bar2 = (ProgressBar) view.findViewById(R.id.comment_ScoreBar2);
		bar3 = (ProgressBar) view.findViewById(R.id.comment_ScoreBar3);
		bar4 = (ProgressBar) view.findViewById(R.id.comment_ScoreBar4);
		bar5 = (ProgressBar) view.findViewById(R.id.comment_ScoreBar5);

		Score1 = (TextView) view.findViewById(R.id.comment_ScoreText1);
		Score2 = (TextView) view.findViewById(R.id.comment_ScoreText2);
		Score3 = (TextView) view.findViewById(R.id.comment_ScoreText3);
		Score4 = (TextView) view.findViewById(R.id.comment_ScoreText4);
		Score5 = (TextView) view.findViewById(R.id.comment_ScoreText5);
		CommentBtn = (Button) view.findViewById(R.id.comment_replyBtn);
		CommentNum = (TextView) view.findViewById(R.id.comment_num);
		pullToRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.pullToRefreshListView);

		new IntroductionTask().execute();

		data = new ArrayList<CommentReply>();
		adapter = new CommentAdapter(getActivity(), data, getActivity(),GameId);
adapter.setComCallback(commcall);
		pullToRefreshListView.getRefreshableView().setAdapter(adapter);
		pullToRefreshListView.setPullLoadEnabled(true);
		pullToRefreshListView.setPullRefreshEnabled(true);
		pullToRefreshListView.getRefreshableView().setSelector(
				new ColorDrawable(android.R.color.transparent));
		pullToRefreshListView.getRefreshableView().setDividerHeight(0);
		pullToRefreshListView.doPullRefreshing(true, 100);
		pullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						if (!NetUtil.isNetworkAvailable(GlobalParams.context)) {
							// 提示网络不给力,直接完成刷新
							PromptManager.showToast(GlobalParams.context,
									"网络不给力");

							pullToRefreshListView.onPullDownRefreshComplete();
						} else {
							// getData(true);
							new CommentTask().execute("Refresh");

						}
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						if (NetUtil.isNetworkAvailable(GlobalParams.context)) {
							((FooterLoadingLayout) pullToRefreshListView
									.getFooterLoadingLayout()).getmHintView()
									.setText("数据加载中...");
							// getData(false);
							new CommentTask().execute("fresh");
						} else {
							PromptManager.showToast(GlobalParams.context,
									"网络不给力");
							pullToRefreshListView.onPullUpRefreshComplete();
						}
					}

				});
		pullToRefreshListView.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				
				
			}
		});
		CommentBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (GlobalParams.hasLogin) {
					userID = GlobalParams.user.getUser_id();
					ReplyDialog replyDia = new ReplyDialog(getActivity());
					replyDia.setReply(false);
					replyDia.setDialogCallback(dialogcallback);
					replyDia.show();

				} else {

					Toast.makeText(getActivity(), "对不起，您没有登录不能评论",
							Toast.LENGTH_SHORT).show();

				}

			}
		});
		return view;
	}

	class CommentTask extends AsyncTask<String, Void, List<CommentReply>> {

		@Override
		protected List<CommentReply> doInBackground(String... params) {

			String isFresh = params[0];

			if (isFresh.equals("Refresh")) {

				data.clear();
				page = 1;

			} else {
				page++;

			}

			CommentApi commentApi = new CommentApi();
			
			getList = commentApi.getCommentsList(getGameId(), page + "", count+ "");

			return getList;

		}

		@Override
		protected void onPostExecute(List<CommentReply> list) {
			super.onPostExecute(list);

			try {
				data.addAll(list);
				adapter.notifyDataSetChanged();
			} catch (Exception e) {
				// TODO: handle exception
			}

			pullToRefreshListView.onPullDownRefreshComplete();
			pullToRefreshListView.onPullUpRefreshComplete();

		}

	}

	class IntroductionTask extends AsyncTask<String, Void, Gameinfo> {

		@Override
		protected Gameinfo doInBackground(String... params) {

			GameApi gameApi = new GameApi();
if(GlobalParams.hasLogin){
	gameInfo = gameApi.getGameInfoList(GlobalParams.user.getUser_id(), GameId);
}else{
	gameInfo = gameApi.getGameInfoList("", GameId);
}
		

			return gameInfo;

		}

		@Override
		protected void onPostExecute(Gameinfo list) {
			super.onPostExecute(list);

			Point = list.getPoint();

			JSONObject obj = JSON.parseObject(list.getStar_info());

			sart1 = obj.getString("star1");
			sart2 = obj.getString("star2");
			sart3 = obj.getString("star3");
			sart4 = obj.getString("star4");
			sart5 = obj.getString("star5");
			totalStar = obj.getString("total_star");

			AllScoreText.setText(Point + "分");

			CommentNum.setText("(共" + totalStar + "份评分)");

			Score1.setText(sart1);
			Score2.setText(sart2);
			Score3.setText(sart3);
			Score4.setText(sart4);
			Score5.setText(sart5);

			int s1 = Integer.parseInt(sart1);
			int s2 = Integer.parseInt(sart2);
			int s3 = Integer.parseInt(sart3);
			int s4 = Integer.parseInt(sart4);
			int s5 = Integer.parseInt(sart5);

			bar1.setProgress(s1);
			bar2.setProgress(s2);
			bar3.setProgress(s3);
			bar4.setProgress(s4);
			bar5.setProgress(s5);

		}

	}

	ReplayDialogcallback dialogcallback = new ReplayDialogcallback() {

		@Override
		public void dialogdo(String replyContent, int arg1,boolean isReply,String commentID) {
			// TODO Auto-generated method stub
			if(isReply){
			
			}else{
				new CommentStarTask().execute(replyContent, arg1 + "");
			}
			
		}

	};

	class CommentStarTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {

			String isFresh = params[0];
			String point = params[1];

			CommentApi comment = new CommentApi();

			String result = comment.setReplyStarList(GameId, point, isFresh,
					userID);

			return result;

		}

		@Override
		protected void onPostExecute(String isStar) {
			super.onPostExecute(isStar);

			if (isStar.equals("1")) {

				Toast.makeText(getActivity(), "评论完成", Toast.LENGTH_SHORT)
						.show();
				pullToRefreshListView.doPullRefreshing(true, 100);
			}

		}

	}
Completecallback commcall=new Completecallback() {
	
	@Override
	public void commplete(boolean isComple) {
		// TODO Auto-generated method stub
		if(isComple){
			pullToRefreshListView.doPullRefreshing(true, 100);
		}
	}
};

	public String getGameId() {
		return GameId;
	}

	public void setGameId(String gameId) {
		GameId = gameId;
	}

}
