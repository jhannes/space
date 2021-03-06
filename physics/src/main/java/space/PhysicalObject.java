package space;

import static java.lang.Math.sqrt;

import java.util.List;

public class PhysicalObject {

    public double mass;
    public double x;
    public double y;
    public double vx;
    public double vy;
    public double radius;
    public static final double G = 6.67428e-11; // m3/kgs2
    public PhysicalObject(double weightKilos, double x, double y, double vx,
                          double vy, double radius) {
        this.mass = weightKilos;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.radius = radius;
    }

    public PhysicalObject absorb(PhysicalObject other) {
        double totalMass = mass + other.mass;
        x = (x * mass + other.x * other.mass) / totalMass;
        y = (y * mass + other.y * other.mass) / totalMass;
        vx = (vx * mass + other.vx * other.mass) / totalMass;
        vy = (vy * mass + other.vy * other.mass) / totalMass;
        mass = totalMass;
        return this;
    }

    public void hitBy(PhysicalObject other, double backstepIncrement) {
        // find collision point by backstepping

        //total backstep size to be found incrementally
        double dt = 0;
        //vector from this object to the other object
        double[] new12 = {x - other.x, y - other.y};
        // new distance
        double d = sqrt(new12[0] * new12[0] + new12[1] * new12[1]);
        // backstep to find collision point
        while (d < radius + other.radius) {
            dt += backstepIncrement;
            new12[0] = new12[0] + backstepIncrement * (vx - other.vx);
            new12[1] = new12[1] + backstepIncrement * (vy - other.vy);
            d = sqrt(new12[0] * new12[0] + new12[1] * new12[1]);
        }

        // simplify variables
        double m1 = other.mass;
        double vx1 = other.vx;
        double vy1 = other.vy;
        // point of impact for other object
        double x1 = other.x + dt * vx1;
        double y1 = other.y + dt * vy1;

        double m2 = mass;
        double vx2 = vx;
        double vy2 = vy;
        // point of impact for this object
        double x2 = x + dt * vx2;
        double y2 = y + dt * vy2;

        // direction of impact
        double[] p12 = {x2 - x1, y2 - y1};
        // normalize p12 to length 1
        double p12_abs = sqrt(p12[0] * p12[0] + p12[1] * p12[1]);
        double[] p12n = {p12[0] / p12_abs, p12[1] / p12_abs};

        // factor in calculation
        double c = p12n[0] * (vx1 - vx2) + p12n[1] * (vy1 - vy2);
        // fully elastic
        double e = 1;
        // new speeds
        double[] v1prim = {vx1 - p12n[0] * (1 + e) * (m2 * c / (m1 + m2)),
                vy1 - p12n[1] * (1 + e) * (m2 * c / (m1 + m2))};
        double[] v2prim = {vx2 + p12n[0] * (1 + e) * (m1 * c / (m1 + m2)),
                vy2 + p12n[1] * (1 + e) * (m1 * c / (m1 + m2))};

        // set variables back
        vx = v2prim[0];
        vy = v2prim[1];

        other.vx = v1prim[0];
        other.vy = v1prim[1];

        // step forward to where the objects should be
        x = x + v2prim[0] * (-dt);
        y = y + v2prim[1] * (-dt);

        other.x = other.x + v1prim[0] * (-dt);
        other.y = other.y + v1prim[1] * (-dt);

    }

    @Override
    public String toString() {
        return "x=" + x + ",y=" + y + ",vx=" + vx + ",vy=" + vy + ",mass="
                + mass + ",radius=" + radius;
    }

    public void applyForce(double fx, double fy, double seconds) {
        double ax = fx / mass;
        double ay = fy / mass;
        x = x - ax * seconds * seconds / 2 + vx * seconds;
        y = y - ay * seconds * seconds / 2 + vy * seconds;
        vx -= ax * seconds;
        vy -= ay * seconds;
    }

    public static void applyGravityForce(List<PhysicalObject> objects, double seconds) {
        for (PhysicalObject aff : objects) {
            aff.applyGravity(objects, seconds);
        }
    }

    protected void applyGravity(List<PhysicalObject> objects, double seconds) {
        double fx = 0;
        double fy = 0;
        for (PhysicalObject oth : objects) {
            if (this == oth) continue;
            double[] d = new double[]{x - oth.x, y - oth.y};
            double r2 = Math.pow(d[0], 2) + Math.pow(d[1], 2);
            double f = G * mass * oth.mass / r2;
            double sqrtOfR2 = Math.sqrt(r2);
            fx += f * d[0] / sqrtOfR2;
            fy += f * d[1] / sqrtOfR2;
        }
        applyForce(fx, fy, seconds);
    }

}
