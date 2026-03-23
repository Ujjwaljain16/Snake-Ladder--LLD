public class EasyMovementStrategy implements MovementStrategy {

    @Override
    public int applyTransitions(int position, Board board, GamePresenter presenter) {
        Integer snakeTail = board.getSnakeTail(position);
        if (snakeTail != null) {
            presenter.displaySnakeEncounter(position, snakeTail);
            return snakeTail;
        }

        Integer ladderEnd = board.getLadderEnd(position);
        if (ladderEnd != null) {
            presenter.displayLadderEncounter(position, ladderEnd);
            return ladderEnd;
        }

        return position;
    }
}
