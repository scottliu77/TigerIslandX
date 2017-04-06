import org.junit.Test;

public class ParserTest {

    @Test
    public void test1() throws Exception{
        Parser testParser = new Parser();
        String message = "WAIT FOR THE TOURNAMENT TO BEGIN 1231321";
        testParser.receiveMessage(message);
        assert(testParser.getPid() == 1231321);

    }

    @Test
    public void test2() throws Exception{
        Parser testParser = new Parser();
        String message = "NEW CHALLENGE 456 YOU WILL PLAY 35 MATCHES";
        testParser.receiveMessage(message);
        assert(testParser.getCid() == 456);
        assert(testParser.getRounds() == 35);

    }
    @Test
    public void test3() throws Exception{
        Parser testParser = new Parser();
        String message = "NEW MATCH BEGINNING NOW YOUR OPPONENT IS PLAYER 1231";
        testParser.receiveMessage(message);
        assert(testParser.getPidOpponent() == 1231);

    }
    @Test
    public void test4() throws Exception{
        Parser testParser = new Parser();
        String message = "MAKE YOUR MOVE IN GAME 11111 WITHIN 222222 SECOND: MOVE 33333 PLACE JUNGLE+ROCKY";
        testParser.receiveMessage(message);


    }


}
