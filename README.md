# 事件分发

- 一个界面上有一个 TextView ，分发机制的流程是怎么样的？
- 一个界面上有一个 Button ，分发机制的流程是怎么样的？

>TextView 的 onTouchEvent() 方法，return false;（clickable = false）<br/>
Button 的 onTouchEvent() 方法，return true;<br/>
Because：View 的 onTouchEvent() 方法都会消费事件，除非它们是不可点击的(clickable 和 longClickable 都为 false)，
默认 longClickable 都为 false，Button 的 clickable 是 true，TextView 的 clickable 是 false。

