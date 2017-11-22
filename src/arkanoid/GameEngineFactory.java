package arkanoid;

public class GameEngineFactory {

    public GameEngine getGameEngine() {
        return new GameEngineImpl();
    }

}
