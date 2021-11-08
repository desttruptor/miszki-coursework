package report;

// вспоминаю что такое коллбеки и как их делать
public class Rffff {
    public Rffff() {
        Callback callBack = new CallbackImpl();
        Caller caller = new Caller(callBack);

        caller.perform();
    }

    interface Callback {
        void callbackFun();
    }

    static class CallbackImpl implements Callback {
        @Override
        public void callbackFun() {
            System.out.println("меня вызвали я коллбек");
        }
    }

    static class Caller {
        Callback callback;
        Caller(Callback callback) {
            this.callback = callback;
        }

        void perform() {
            callback.callbackFun();
        }
    }
}
