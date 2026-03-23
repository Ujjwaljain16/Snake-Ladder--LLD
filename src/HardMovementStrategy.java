public class HardMovementStrategy implements MovementStrategy {

    @Override
    public int applyTransitions(int position, Board board, GamePresenter presenter) {
        int current = position;
        int safety = 0;

        while (safety <= board.getTotalCells()) {
            Integer snakeTail = board.getSnakeTail(current);
            if (snakeTail != null) {
                presenter.displaySnakeEncounter(current, snakeTail);
                current = snakeTail;
                safety++;
                continue;
            }

            Integer ladderEnd = board.getLadderEnd(current);
            if (ladderEnd != null) {
                presenter.displayLadderEncounter(current, ladderEnd);
                current = ladderEnd;
                safety++;
                continue;
            }

            break;
        }

        return current;
    }
}
