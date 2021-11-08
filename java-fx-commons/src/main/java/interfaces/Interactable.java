package interfaces;

public interface Interactable extends MyEventTarget, MouseTarget, KeyEventTarget,
        InputMethodEventTarget, RotateEventTarget, ZoomEventTarget, SwipeEventTarget, TouchEventTarget,
        DragEventTarget, ContextMenuEventTarget {

}
