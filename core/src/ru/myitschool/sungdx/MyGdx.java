package ru.myitschool.sungdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

import org.w3c.dom.ls.LSOutput;

public class MyGdx extends ApplicationAdapter {
	public static final int SCR_WIDTH = 1280, SCR_HEIGHT = 720;

	SpriteBatch batch;
	OrthographicCamera camera;
	Vector3 touch;
	BitmapFont font;

	Texture[] imgMosq = new Texture[11];
	Texture imgBG;
	Sound[] sndMosq = new Sound[3];

	Mosquito[] mosq = new Mosquito[100];
	Player[] players = new Player[6];
	Player player;
	int frags;
	long timeStart, timeCurrent;
	boolean gameOver = false;

	@Override
	public void create () {
		// создание системных объектов
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
		touch = new Vector3();
		generateFont();

		// создание изображений
		imgBG = new Texture("background.jpg");
		for (int i = 0; i < imgMosq.length; i++) {
			imgMosq[i] = new Texture("mosq"+i+".png");
		}

		// создание звуков
		for (int i = 0; i < sndMosq.length; i++) {
			sndMosq[i] = Gdx.audio.newSound(Gdx.files.internal("mosq"+i+".mp3"));
		}

		// создание игроков для таблицы рекордов
		for (int i = 0; i < players.length; i++) {
			players[i] = new Player("Никто", 0);
		}
		player = new Player("Gamer", 0);

		gameStart();
	}


	@Override
	public void render () {
		// обработка касаний экрана
		if(Gdx.input.justTouched()){
			touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touch);
			if(gameOver){
				gameStart();
			} else {
				for (int i = mosq.length - 1; i >= 0; i--) {
					if (mosq[i].isAlive && mosq[i].hit(touch.x, touch.y)) {
						frags++;
						sndMosq[MathUtils.random(0, 2)].play();
						if (frags == mosq.length) gameOver();
						break;
					}
				}
			}
		}

		// события игры
		for (int i = 0; i < mosq.length; i++) {
			mosq[i].move();
		}
		if(!gameOver) {
			timeCurrent = TimeUtils.millis() - timeStart;
		}

		// отрисовка всей графики
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
		for (int i = 0; i < mosq.length; i++) {
			batch.draw(imgMosq[mosq[i].faza], mosq[i].getX(), mosq[i].getY(), mosq[i].width, mosq[i].height, 0, 0, 500, 500, mosq[i].isFlip(), false);
		}
		font.draw(batch, "УБИЙСТВА: "+frags, 10, SCR_HEIGHT-10);
		font.draw(batch, timeToString(timeCurrent), SCR_WIDTH-200, SCR_HEIGHT-10);
		if(gameOver) font.draw(batch, tableOfRecordsToString(), SCR_WIDTH/3f, SCR_HEIGHT/4f*3);
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		for (int i = 0; i < imgMosq.length; i++) {
			imgMosq[i].dispose();
		}
	}

	void generateFont(){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("21028.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.color = new Color(1, 0.8f, 0.4f, 1);
		parameter.size = 50;
		parameter.borderColor = Color.BLACK;
		parameter.borderWidth = 2;
		parameter.borderStraight = true;
		parameter.shadowColor = new Color(0.1f, 0.1f, 0.1f, 0.8f);
		parameter.shadowOffsetX = parameter.shadowOffsetY = 3;
		String str = "";
		for (char i = 0x20; i < 0x7B; i++) str += i;
		for (char i = 0x401; i < 0x452; i++) str += i;
		parameter.characters = str;
		font = generator.generateFont(parameter);
		generator.dispose();
	}

	String timeToString(long time){
		return time/1000/60/60 + ":" + time/1000/60%60/10 + time/1000/60%60%10 + ":" + time/1000%60/10 + time/1000%60%10;
	}

	void gameOver(){
		gameOver = true;
		player.time = timeCurrent;

		class MyTextListener implements Input.TextInputListener{
			@Override
			public void input(String text) {
				player.name = text;
				players[players.length-1].time = player.time;
				players[players.length-1].name = player.name;
				sortPlayers();
			}

			@Override
			public void canceled() {

			}
		}

		Gdx.input.getTextInput(new MyTextListener(), "Введите имя", player.name, "");
	}

	void gameStart(){
		gameOver = false;
		frags = 0;
		timeStart = TimeUtils.millis();
		// создание комаров
		for (int i = 0; i < mosq.length; i++) {
			mosq[i] = new Mosquito();
		}
	}

	void sortPlayers(){
		for (int i = 0; i < players.length; i++) if(players[i].time == 0) players[i].time = Long.MAX_VALUE;

		for (int j = 0; j < players.length; j++) {
			for (int i = 0; i < players.length-1; i++) {
				if(players[i].time>players[i+1].time){
					Player c = players[i];
					players[i] = players[i+1];
					players[i+1] = c;
				}
			}
		}
		for (int i = 0; i < players.length; i++) if(players[i].time == Long.MAX_VALUE) players[i].time = 0;
	}
	String tableOfRecordsToString(){
		String s = "";
		for (int i = 0; i < players.length-1; i++) {
			s += players[i].name+"........"+timeToString(players[i].time)+"\n";
		}
		return s;
	}
}
