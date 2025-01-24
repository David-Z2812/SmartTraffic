package smartroad.types;
import java.util.List;

public enum ESmartroadSegments {
    R1s1("R1s1", List.of(0, 10, 20, 25, 35, 50), 40),
    R1s2a("R1s2a", List.of(20, 170, 320), 60),
    R1s2d("R1s2d", List.of(20, 25), 60),
    R1s3("R1s3", List.of(0, 10, 20, 25, 35, 40, 50), 40),
    R1s4a("R1s4a", List.of(340, 490, 640), 60),
    R1s4d("R1s4d", List.of(490, 640), 60),
    R1s5("R1s5", List.of(0, 10, 20, 25, 35, 40, 50), 40),
    R1s6a("R1s6a", List.of(660, 830, 960, 1110), 60),
    R1s6d("R1s6d", List.of(660, 830, 1110), 60),
    R1s7("R1s7", List.of(0, 10, 25, 35, 40, 45, 50), 40),
    R1s8a("R1s8a", List.of(1130, 1250, 1360), 60),
    R1s8d("R1s8d", List.of(1130, 1250), 60),
    R2s1("R2s1", List.of(0, 170, 300), 60),
    R2s2("R2s2", List.of(300, 750), 60),
    R3s1("R3s1", List.of(0, 170, 210), 60),
    R4s1("R4s1", List.of(0, 35, 355), 60),
    R5s1("R5s1", List.of(0, 490, 580), 60),
    R6s1("R6s1", List.of(0, 35, 210), 60),
    R7s1a("R7s1a", List.of(0, 375), 60),
    R7s1d("R7s1d", List.of(0, 375), 60),
    R7s2("R7s2", List.of(0, 20, 35, 45, 50), 40),
    R7s3("R7s3", List.of(20, 550, 700), 60),
    R7s4("R7s4", List.of(0, 20, 30, 50), 40),
    R8s1a("R8s1a", List.of(0, 40, 210), 60),
    R8s1d("R8s1d", List.of(0, 35, 210), 60),
    R9s1("R9s1", List.of(0, 415), 60),
    R10s1("R10s1", List.of(0, 210), 60),
    R11s1("R11s1", List.of(0, 60, 210), 60),
    R12s1a("R12s1a", List.of(0, 375, 395), 80),
    R12s1d("R12s1d", List.of(0, 15, 395), 80),
    R12s2("R12s2", List.of(375, 395), 80),
    R13s1("R13s1", List.of(0, 375), 60),
    R14s1("R14s1", List.of(0, 500), 60),
    R15s1("R15s1", List.of(0, 105), 30),
    R16s1a("R16s1a", List.of(0, 210), 60),
    R16s1d("R16s1d", List.of(0, 210), 60),
    R17s1("R17s1", List.of(0, 10, 20, 25, 30, 45, 50), 40),
    R17s2a("R17s2a", List.of(20, 30), 40),
    R17s2d("R17s2d", List.of(20, 165, 290, 435), 60),
    R17s3("R17s3", List.of(0, 25, 35, 45, 50), 40),
    R17s4("R17s4", List.of(455, 1060), 80),
    R18s1("R18s1", List.of(0, 210), 60),
    R19s1("R19s1", List.of(0, 170), 60),
    R20s1a("R20s1a", List.of(0, 60), 60),
    R20s1d("R20s1d", List.of(0, 60), 60),
    R21s1("R21s1", List.of(0, 10, 20, 25, 30, 40, 50), 40),
    R21s2a("R21s2a", List.of(20, 310), 60),
    R21s2d("R21s2d", List.of(0, 25), 60),
    R21s3("R21s3", List.of(0, 20, 45, 50), 40),
    R21s4a("R21s4a", List.of(330, 1300, 1500), 80),
    R21s4d("R21s4d", List.of(330, 1500), 80),
    R22s1a("R22s1a", List.of(0, 150), 60),
    R22s1d("R22s1d", List.of(0, 150), 60),
    Re1s1("Re1s1", List.of(0, 150), 80),
    Re2s1a("Re2s1a", List.of(0, 300), 60),
    Re2s1d("Re2s1d", List.of(0, 300), 60),
    Re3s1a("Re3s1a", List.of(0, 200), 60),
    Re3s1d("Re3s1d", List.of(0, 200), 60),
    Re4s1("Re4s1", List.of(0, 300), 80),
    Re5s1("Re5s1", List.of(0, 45, 50), 50);

    private final String segment;
    private final List<Integer> points;
    private final int maxSpeed;

    ESmartroadSegments(String segment, List<Integer> points, int maxSpeed) {
        this.segment = segment;
        this.points = points;
        this.maxSpeed = maxSpeed;
    }

    @Override
    public String toString() {
        return this.segment;
    }

    public Integer getPoint(int index) {
        if (index >= 0 && index < points.size()) {
            return points.get(index);
        } else {
            throw new IndexOutOfBoundsException("Index out of range: " + index);
        }
    }

    public int getMaxSpeed() {
        return this.maxSpeed;
    }
}
