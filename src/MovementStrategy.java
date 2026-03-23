public interface MovementStrategy {
    int applyTransitions(int position, Board board, GamePresenter presenter);
}
