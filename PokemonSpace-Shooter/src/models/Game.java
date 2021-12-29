package models;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.JavaBean;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.applet.AudioClip;

import javax.swing.Timer;

public class Game {

	private CopyOnWriteArrayList<Pokemon> pokemonsList;
	private CopyOnWriteArrayList<Bullet> bulletsList;
	private Power currentPower;
	private int[] scoresList;
	private Timer countDownTimer;
	private Timer powerTimer;
	private int seconds;
	private int minutes;
	private int pokemonsAmount;
	private int speed;
	private boolean timeFinished;
	private boolean powerPopUp;
	private Player player;
	private AudioClip shotAudio;
	private AudioClip plusLiveAudio;
	private AudioClip reloadAudio;
	private AudioClip emptyGunAudio;
	private AudioClip explosionAudio;
	private AudioClip killedAudio;
	private static final String AUDIO_SHOT_SOUND_WAV = "/audio/shotSound.wav";
	private static final String AUDIO_KILLED_WAV = "/audio/killed.wav";
	private static final String AUDIO_EXPLOSION2_WAV = "/audio/explosion2.wav";
	private static final String AUDIO_EMPTY_GUN_WAV = "/audio/emptyGun.wav";
	private static final String AUDIO_RELOAD_WAV = "/audio/reload.wav";
	private static final String AUDIO_PLUS_LIVE_WAV = "/audio/plusLive.wav";

	public Game(int pokemonsAmount, int speed) {
		scoresList = new int[7];
		this.pokemonsAmount = pokemonsAmount;
		this.speed = speed;
		initSounds();
		initPokemonsList(pokemonsAmount);
		player = new Player();
		seconds = 1;
		minutes = 1;
		timeFinished = false;
		initTimer();
	}

	private void initSounds() {
		shotAudio = java.applet.Applet.newAudioClip(getClass().getResource(AUDIO_SHOT_SOUND_WAV));
		plusLiveAudio = java.applet.Applet.newAudioClip(getClass().getResource(AUDIO_PLUS_LIVE_WAV));
		reloadAudio = java.applet.Applet.newAudioClip(getClass().getResource(AUDIO_RELOAD_WAV));
		emptyGunAudio = java.applet.Applet.newAudioClip(getClass().getResource(AUDIO_EMPTY_GUN_WAV));
		explosionAudio = java.applet.Applet.newAudioClip(getClass().getResource(AUDIO_EXPLOSION2_WAV));
		killedAudio = java.applet.Applet.newAudioClip(getClass().getResource(AUDIO_KILLED_WAV));
	}

	private void initPowerTimer() {
		powerTimer = new Timer(50, new ActionListener() {
			long initMillis = System.currentTimeMillis();

			@Override
			public void actionPerformed(ActionEvent e) {
				if (System.currentTimeMillis() - initMillis > 5000) {
					currentPower = null;
					powerPopUp = false;
					powerTimer.stop();
				} else {
					if (!powerPopUp) {
						popUpPower();
					}
				}
			}
		});
		powerTimer.start();
	}

	private void initTimer() {
		countDownTimer = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (seconds % 10 == 0) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							addMorePokemons();
						}
					}).start();
				}
				if (seconds % 15 == 0) {
					initPowerTimer();
				}
				if (minutes == 0 && seconds == 0) {
					timeFinished = true;
					countDownTimer.stop();
				} else if (seconds == 0) {
					minutes--;
					seconds += 60;
					seconds--;
				} else {
					seconds--;
				}
			}
		});
	}

	public void startCountDown() {
		countDownTimer.start();
	}

	private void initPokemonsList(int amount) {
		pokemonsList = new CopyOnWriteArrayList<>();
		bulletsList = new CopyOnWriteArrayList<>();
		double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		double width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double pokemonInitLocationY = ThreadLocalRandom.current().nextDouble(height / 100,
				height - height / 4 - (width + height) / 30);
		double pokemonInitLocationX = ThreadLocalRandom.current().nextDouble(width / 2, width - (width + height) / 30);
		for (int i = 0; i < amount; i++) {
			Pokemon circle = new Pokemon(pokemonInitLocationY, pokemonInitLocationX, speed, this);
			pokemonsList.add(circle);
		}
	}

	private void popUpPower() {
		powerPopUp = true;
		currentPower = new Power();
	}

	private void addMorePokemons() {
		int amount = pokemonsAmount;
		double height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		double width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double pokemonInitLocationY = ThreadLocalRandom.current().nextDouble((width + height) / 30,
				height - height / 4 - (width + height) / 30);
		double pokemonInitLocationX = ThreadLocalRandom.current().nextDouble(width / 2, width - (width + height) / 30);
		for (int i = 0; i < amount; i++) {
			Pokemon circle = new Pokemon(pokemonInitLocationY, pokemonInitLocationX, speed, this);
			pokemonsList.add(circle);
		}
	}

	public void verifyCrashing() {
		verifyPlayerCrashed();
		verifyPowerCrashed();
	}

	public void verifyPlayerCrashed() {
		for (Pokemon pokemon : pokemonsList) {
			if ((pokemon.isAlive() && !player.isImmune())
					&& (getCrashDistance(player.getCenterX(), player.getCenterY(), pokemon.getCenterX(),
							pokemon.getCenterY()) <= (pokemon.crashingSize() + player.crashingSize()) / 2)) {
				player.die();
				pokemon.die();
				killedAudio.play();
				pokemon.initExplosion();
				player.startImmuneTimer();
			}
		}
	}

	public void verifyPowerCrashed() {
		if (powerPopUp && getCrashDistance(player.getCenterX(), player.getCenterY(), currentPower.getCenterX(),
				currentPower.getCenterY()) <= (player.crashingSize() + currentPower.crashingSize()) / 2) {
			powerTimer.stop();
			powerPopUp = false;
			if (currentPower.getType() == 2) {
				if (player.getLives() < 5) {
					player.addLive();
					plusLiveAudio.play();
				}
			} else {
				player.addBullets();
				reloadAudio.play();
			}
			currentPower = null;
		}
	}

	public void verifyShootCrashing(Bullet bullet) {
		for (Pokemon pokemon : pokemonsList) {
			if ((bullet.isAlive() && pokemon.isAlive()) && getCrashDistance(bullet.getCenterX(), bullet.getCenterY(),
					pokemon.getCenterX(), pokemon.getCenterY()) <= (pokemon.getSize() + bullet.getSize()) / 2) {
				bullet.die();
				bulletsList.remove(bullet);
				scoresList[pokemon.getType() - 1] += 1;
				pokemon.die();
				pokemon.initExplosion();
				explosionAudio.play();
			}
		}
	}

	public void removePokemon(Pokemon pokemon) {
		pokemonsList.remove(pokemon);
	}

	private double getCrashDistance(double x1, double y1, double x2, double y2) {
		double a = Math.pow(x1 - x2, 2);
		double b = Math.pow(y1 - y2, 2);
		return Math.abs(Math.sqrt(a + b));
	}

	public void addBullet(double xCursor, double yCursor) {
		if (player.getBullets() > 0) {
			shotAudio.play();
			xCursor += 16;
			yCursor += 16;
			double xDistance = (player.getX() + player.getSize() / 2) - xCursor;
			double yDistance = (player.getY() + player.getSize() / 2) - yCursor;
			if (xCursor < player.getX() + player.getSize() / 2) {
				bulletsList.add(new Bullet(player.crashingX() + player.crashingSize() / 2,
						player.crashingY() + player.crashingSize() / 2, xDistance, yDistance, true, this));
			} else {
				bulletsList.add(new Bullet(player.crashingX() + player.crashingSize() / 2,
						player.crashingY() + player.crashingSize() / 2, xDistance, yDistance, false, this));
			}
			player.spendBullet();
		} else {
			emptyGunAudio.play();
		}
	}

	public void moveGun(double xCursor, double yCursor) {
		double xDistance = (player.getX() + player.getSize() / 2) - xCursor;
		double yDistance = (player.getY() + player.getSize() / 2) - yCursor;
		if (xCursor < player.getX() + player.getSize() / 2) {
			player.rotateGun(Math.atan(yDistance / xDistance), true);
		} else {
			player.rotateGun(Math.atan(yDistance / xDistance), false);
		}
	}

	public void cleanCircles() {
		pokemonsList.clear();
	}

	public void stopAllThreads() {
		countDownTimer.stop();
		if (powerTimer != null) {
			powerTimer.stop();
		}
		for (Pokemon pokemon : pokemonsList) {
			pokemon.stop();
		}
	}

	// ========================================= GETTERS && SETTERS
	// ==========================================

	public CopyOnWriteArrayList<Pokemon> getPokemonsList() {
		return pokemonsList;
	}

	public Player getPlayer() {
		return player;
	}

	public CopyOnWriteArrayList<Bullet> getBulletsList() {
		return bulletsList;
	}

	public boolean isTimeFinished() {
		return timeFinished;
	}

	public int getSeconds() {
		return seconds;
	}

	public int getMinutes() {
		return minutes;
	}

	public int[] getScoresList() {
		return scoresList;
	}

	public Power getCurrentPower() {
		return currentPower;
	}

	public int getScore() {
		int score = 0;
		for (int i = 0; i < scoresList.length; i++) {
			score += scoresList[i];
		}
		return score;
	}
}
