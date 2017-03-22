import java.awt.*;

/**
 * Created by Wylie on 3/22/2017.
 */
public interface BuildAction
{
    public void execute(Board board, Player player, Point target);
}


class FoundSettlementAction implements BuildAction
{
    public FoundSettlementAction()
    {

    }
    public void execute(Board board, Player player, Point target)
    {
        HexButton hexButton = board.getHexButton(target);

    }
}
