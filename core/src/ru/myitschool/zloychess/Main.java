package ru.myitschool.zloychess;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class Main extends ApplicationAdapter {
	// base: host1674609_zlayadb
	// user: host1674609_zloyuser
	// pass: zloychesspass
	public static final int SCR_WIDTH = 720/4*3, SCR_HEIGHT = 1280/4*3;
	public static final int WHITE = 0, BLACK = 1;
	public static final int OUR_SIDE = 0, OTHER_SIDE = 1;
	public static final int PAWN = 0, BISHOP = 1, HORSE = 2, ROOK = 3, QUEEN = 4, KING = 5;
	int[] figuresOnBoard = {0, 0, 0, 0, 0, 0, 0, 0,
			        		3, 2, 1, 4, 5, 1, 2, 3,
							0, 0, 0, 0, 0, 0, 0, 0,
							3, 2, 1, 4, 5, 1, 2, 3};
	SpriteBatch batch;
	OrthographicCamera camera;
	Vector3 touch;
	ChessInputProcessor input;

	Texture imgAtlas;
	TextureRegion[] imgCell = new TextureRegion[2];
	TextureRegion[][] imgFigure = new TextureRegion[2][6];
	Texture[] imgButton = new Texture[2];

	static float size;
	static float paddingBottom;
	Cell[][] board = new Cell[8][8];
	Figure[][] figure = new Figure[2][16];
	int curIndex, curSide;
	ChessButton buttonCreate;
	//int side;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		touch = new Vector3();
		camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
		input = new ChessInputProcessor();
		Gdx.input.setInputProcessor(input);

		imgAtlas = new Texture("zloychessatlas.png");
		for(int i=0; i<2; i++) {
			imgCell[i] = new TextureRegion(imgAtlas, 1400, i * 200, 200, 200);
			for(int j=0; j<6; j++) imgFigure[i][j] = new TextureRegion(imgAtlas, j*200, i*200, 200, 200);
		}
		imgButton[0] = new Texture("button_create.png");
		imgButton[1] = new Texture("button_create_on.png");

		size = SCR_WIDTH/8f;
		paddingBottom = (SCR_HEIGHT - SCR_WIDTH)/2;

		buttonCreate = new ChessButton(20, 20, 150, 50);

		for(int j = 0; j<8; j++)
			for(int i=0; i<8; i++)
				board[i][j] = new Cell(i, j, (i+(j+1)%2)%2, null);

		int ourColor = MathUtils.random(WHITE, BLACK);
		int otherColor;
		if(ourColor == WHITE) otherColor = BLACK;
		else {
			otherColor = WHITE;
			figuresOnBoard[11] = figuresOnBoard[27] = KING;
			figuresOnBoard[12] = figuresOnBoard[28] = QUEEN;
		}

		for(int i=0; i<8; i++) board[i][1].figure = figure[OUR_SIDE][i] = new Figure(i, 1, ourColor, figuresOnBoard[i], OUR_SIDE);
		for(int i=8; i<16; i++) board[i-8][0].figure = figure[OUR_SIDE][i] = new Figure(i-8, 0, ourColor, figuresOnBoard[i], OUR_SIDE);
		for(int i=0; i<8; i++) board[i][6].figure = figure[OTHER_SIDE][i] = new Figure(i, 6, otherColor, figuresOnBoard[i+16], OTHER_SIDE);
		for(int i=8; i<16; i++) board[i-8][7].figure = figure[OTHER_SIDE][i] = new Figure(i-8, 7, otherColor, figuresOnBoard[i+16], OTHER_SIDE);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.2f, 0, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(imgButton[buttonCreate.pressed], buttonCreate.x, buttonCreate.y, buttonCreate.width, buttonCreate.height);

		for(int j = 0; j<8; j++)
			for(int i=0; i<8; i++)
				batch.draw(imgCell[board[i][j].color], i*size, j*size + paddingBottom, size, size);

		for(int i=0; i<2; i++)
			for(int j=0; j<16; j++)
				batch.draw(imgFigure[figure[i][j].color][figure[i][j].type], figure[i][j].scrX, figure[i][j].scrY, size, size);
		batch.draw(imgFigure[figure[curSide][curIndex].color][figure[curSide][curIndex].type], figure[curSide][curIndex].scrX, figure[curSide][curIndex].scrY, size, size);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		imgAtlas.dispose();
	}

	class ChessInputProcessor implements InputProcessor{

		@Override
		public boolean keyDown(int keycode) {
			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			return false;
		}

		@Override
		public boolean keyTyped(char character) {
			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			touch.set(screenX, screenY, 0);
			camera.unproject(touch);
			if(buttonCreate.isHit(touch.x, touch.y)){
				buttonCreate.pressed = 1;
			}

			for(int i=0; i<2; i++)
				for(int j=0; j< 16; j++)
					if(figure[i][j].isHit(touch.x, touch.y)) {
						figure[i][j].isMove = true;
						curIndex = j;
						curSide = i;
					}
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			touch.set(screenX, screenY, 0);
			camera.unproject(touch);

			if(buttonCreate.isHit(touch.x, touch.y)){
				buttonCreate.pressed = 0;
				// подключаемся к базе
			}

			if(figure[curSide][curIndex].isMove){
				if(figure[curSide][curIndex].isDropCorrect(touch.x, touch.y, board)){
					board[figure[curSide][curIndex].boardX][figure[curSide][curIndex].boardY].figure = null;
					figure[curSide][curIndex].screenToBoard();
					board[figure[curSide][curIndex].boardX][figure[curSide][curIndex].boardY].figure = figure[curSide][curIndex];
				}
				figure[curSide][curIndex].isMove = false;
				figure[curSide][curIndex].boardToScreen();
			}
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			touch.set(screenX, screenY, 0);
			camera.unproject(touch);
			if(figure[curSide][curIndex].isMove) figure[curSide][curIndex].move(touch.x, touch.y);
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			return false;
		}

		@Override
		public boolean scrolled(float amountX, float amountY) {
			return false;
		}
	}
}
