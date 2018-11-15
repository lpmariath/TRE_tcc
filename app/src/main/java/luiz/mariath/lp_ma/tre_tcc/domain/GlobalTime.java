package luiz.mariath.lp_ma.tre_tcc.domain;

public class GlobalTime {
    private static GlobalTime instance;

    private double time;

    private GlobalTime(){

    }

    public static synchronized GlobalTime getInstance() {
        if(instance==null){
            instance=new GlobalTime();
        }
        return instance;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
