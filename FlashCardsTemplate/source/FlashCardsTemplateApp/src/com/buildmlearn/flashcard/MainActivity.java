package com.buildmlearn.flashcard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockActivity implements AnimationListener {
	View answerView, questionView;
	Button flipButton;
	Button preButton;
	Button nextButton;
	boolean isFlipped = false;
	int iQuestionIndex = 0;
	GlobalData gd = GlobalData.getInstance();
	String flashCardanswer;
	ImageView questionImage;
	TextView flashCardText, flashcardNumber;
	TextView questionText;
	private Animation animation1;
	private Animation animation2;
	private View currentView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		animation1 = AnimationUtils.loadAnimation(this, R.anim.to_middle);
		animation1.setAnimationListener(this);
		animation2 = AnimationUtils.loadAnimation(this, R.anim.from_middle);
		animation2.setAnimationListener(this);

		questionView = (View) findViewById(R.id.questionInMain);
		answerView = (View) findViewById(R.id.answerInMain);

		questionView.setVisibility(View.VISIBLE);
		answerView.setVisibility(View.GONE);
		iQuestionIndex = 0;

		questionImage = (ImageView) findViewById(R.id.questionImage);
		flashCardText = (TextView) findViewById(R.id.flashCardText);
		questionText = (TextView) findViewById(R.id.questionhint);
		flashcardNumber = (TextView) findViewById(R.id.flashCardNumber);

		flipButton = (Button) findViewById(R.id.flip_button);
		preButton = (Button) findViewById(R.id.pre_button);
		nextButton = (Button) findViewById(R.id.next_button);

		populateQuestion(iQuestionIndex);
		currentView = questionView;

		flipButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				currentView.clearAnimation();
				currentView.setAnimation(animation1);
				currentView.startAnimation(animation1);

			}
		});

		preButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (iQuestionIndex != 0) {
					isFlipped = false;

					iQuestionIndex--;
					questionView.setVisibility(View.VISIBLE);
					answerView.setVisibility(View.GONE);
					currentView = questionView;

					populateQuestion(iQuestionIndex);
				}

			}
		});

		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (iQuestionIndex < gd.iQuizList.size() - 1) {
					isFlipped = false;
					iQuestionIndex++;
					questionView.setVisibility(View.VISIBLE);
					answerView.setVisibility(View.GONE);
					currentView = questionView;
					populateQuestion(iQuestionIndex);

				} else {
					Intent myIntent = new Intent(MainActivity.this,
							ScoreActivity.class);
					startActivity(myIntent);
					reInitialize();
					finish();
				}

			}
		});
	}

	public void populateQuestion(int index) {
		if (index == 0) {
			preButton.setEnabled(false);

		} else
			preButton.setEnabled(true);

		int cardNum = index + 1;
		flashcardNumber.setText("Card #" + cardNum + " of " + gd.totalCards);
		String[] dataLine = gd.iQuizList.get(index).split("__");
		if (dataLine[0].equals("IMAGE")) {
			questionImage.setVisibility(View.VISIBLE);
			Resources r = getResources();
			int picId = r.getIdentifier("image" + String.valueOf(index),
					"drawable", "com.buildmlearn.flashcard");

			questionImage.setImageResource(picId);
		} else {
			questionImage.setVisibility(View.GONE);

		}
		String[] dataArray = dataLine[1].split("==");
		// TextView answerText = (TextView) findViewById(R.id.answerText);
		// answerText.setText(dataArray[1]);
		flashCardanswer = dataArray[1];

		flashCardText.setText(dataArray[0]);

		questionText.setVisibility(View.GONE);
		if (dataArray.length == 3) {
			questionText.setVisibility(View.VISIBLE);
			questionText.setText(dataArray[2]);
		}

	}

	public void reInitialize() {
		iQuestionIndex = 0;
		gd.iQuizList.clear();
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		if (animation == animation1) {

			TextView answerText = (TextView) findViewById(R.id.answerText);

			if (!isFlipped) {

				answerView.setVisibility(View.VISIBLE);
				questionView.setVisibility(View.GONE);
				isFlipped = true;
				answerText.setText(flashCardanswer);
				currentView = answerView;
			} else {

				isFlipped = false;
				answerText.setText("");
				questionView.setVisibility(View.VISIBLE);
				answerView.setVisibility(View.GONE);
				currentView = questionView;
			}
			currentView.clearAnimation();
			currentView.setAnimation(animation2);
			currentView.startAnimation(animation2);

		}

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getSupportMenuInflater().inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_info) {

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					MainActivity.this);

			// set title
			alertDialogBuilder.setTitle("About Us");

			// set dialog message
			alertDialogBuilder
					.setMessage(getString(R.string.about_us))
					.setCancelable(false)
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									dialog.dismiss();
								}
							});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			// show it
			alertDialog.show();
			TextView msg=(TextView) alertDialog.findViewById(android.R.id.message);
			Linkify.addLinks(msg, Linkify.WEB_URLS);
			
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

}
