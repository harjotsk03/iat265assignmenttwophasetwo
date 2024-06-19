import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.Timer;

import processing.core.PVector;

public class Orca {

    private PVector pos;
    private PVector speed;
    private int size;
    private double scalex = 1;
    private double scaley = 1;
    private Color bodyColor;
    private Color bodyDarkerColor;
    Color hasHitColor = new Color(153, 204, 255);
    private Ellipse2D.Double bodyShape;
    private Ellipse2D.Double eyeLeft;
    private Ellipse2D.Double eyeRight;
    private Ellipse2D.Double finTop;
    private Ellipse2D.Double finBottom;

    private float speedLimit = 1.0f; // Adjust speed limit as needed
    private float wallCoef = 50.0f; // Adjust wall coefficient as needed

    private Area outline;
    private Timer hitTimer;
    private boolean hasHit = false;
    private boolean secondFood = false;
    private Arc2D.Double fov;
    private float maxSpeed;

    public Orca(int x, int y, int size, int speedx, int speedy) {
        pos = new PVector(x, y);
        this.size = size;
        this.speed = new PVector(speedx, speedy);

        // Initialize shapes
        bodyShape = new Ellipse2D.Double();
        eyeLeft = new Ellipse2D.Double();
        eyeRight = new Ellipse2D.Double();
        finTop = new Ellipse2D.Double();
        finBottom = new Ellipse2D.Double();

        while (maxSpeed == 0) this.maxSpeed = (float) Util.random(3,5);

        // Colors
        bodyColor = new Color(153, 204, 255);
        bodyDarkerColor = new Color(102, 153, 204);

        setShapeAttributes();

        hitTimer = new Timer(4000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setHasHit(false);
            }
        });
        hitTimer.setRepeats(false);
    }

    private void setShapeAttributes() {
        bodyShape.setFrame(-size / 2, -size / 2, size, size);
        eyeLeft.setFrame(size / 20, size / 15, size / 14, size / 14);
        eyeRight.setFrame(-size / 20, size / 15, size / 14, size / 14);
        finTop.setFrame(0, -50, 20, 80);
        finBottom.setFrame(-50, -30, 20, 40);

        float sight = 40 * maxSpeed * .75f;
		fov = new Arc2D.Double(-sight, -sight, sight*2, sight*2, -55, 110, Arc2D.PIE);

        outline = new Area(bodyShape);
        outline.add(new Area(finTop));
        outline.add(new Area(finBottom));
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        AffineTransform old = g2.getTransform();

        g2.translate(pos.x, pos.y);
        g2.setColor(Color.red);
        g2.rotate(speed.heading());
        g2.scale(scalex, scaley);
        if (speed.x < 0) g2.scale(1, -1);

        if(hasHit){
            g2.setColor(hasHitColor);
        }else{
            g2.setColor(bodyColor);
        }

        g2.fill(bodyShape);

        g2.setColor(Color.black);
        g2.translate(size / 2 - size / 5, -size / 8);
        g2.fill(eyeLeft);
        g2.fill(eyeRight);

        // Draw mouth
        g2.setStroke(new BasicStroke(2));
        g2.drawArc(-size / 60, 20, 10, 15, 0, -180);

        // Draw fins
        if(hasHit){
            g2.setColor(hasHitColor);
        }else{
            g2.setColor(bodyColor);
        }
        g2.translate(-size / 2 - size / 4, size / 2 + size / 25);
        g2.rotate(-Math.PI / 3);
        g2.fill(finTop);
        g2.rotate(Math.PI / 2);
        g2.fill(finBottom);

        // g2.setColor(Color.red);
		// g2.draw(fov);

        g2.setTransform(old);
    }

    public void move(Dimension panelSize, ArrayList<Fish> fishList, ArrayList<Orca> orcaList) {
        Fish target = null;
        float closestDist = Float.MAX_VALUE;
        Orca biggerOrca = null;
        float closestOrcaDist = Float.MAX_VALUE;
    
        if (!hasHit) {
            for (Fish food : fishList) {
                PVector foodLoc = food.getLocation();
                float dist = PVector.dist(pos, foodLoc);
    
                if (dist < closestDist) {
                    closestDist = dist;
                    target = food;
                } else if (dist == closestDist && food.getSize() > target.getSize()) {
                    // If distances are equal, prefer the larger food
                    closestDist = dist;
                    target = food;
                }
            }
    
            if (target != null) {
                moveToTarget(target);
            }
        }
    
        for (Orca other : orcaList) {
            if (other == this) continue;
            PVector otherLoc = other.getLocation();
            float dist = PVector.dist(pos, otherLoc);
    
            if (dist < size / 2 + other.size / 2) {
                if (other.size > size) {
                    biggerOrca = other;
                    closestOrcaDist = dist;
                    if (!other.hasHit) {
                        setHasHit(true);
                    }
                } else {
                    PVector pushForce = PVector.sub(pos, otherLoc);
                    pushForce.normalize();
                    pushForce.mult(3.0f);
                    speed.add(pushForce);
                    speed.limit(speedLimit);
                }
            }
        }
    
        if (biggerOrca != null) {
            // Calculate push force away from the bigger orca
            PVector awayForce = PVector.sub(pos, biggerOrca.getLocation());
            awayForce.normalize();
            
            // Adjust the avoidance factor to control how strong the repulsion is
            float avoidanceFactor = 0.5f; // Adjust as needed
            awayForce.mult(avoidanceFactor);
            
            speed.add(awayForce);
            speed.limit(speedLimit);
            checkCollision(panelSize);
        }
    
        // applyWallAvoidance(panelSize);
        avoidCollisions(orcaList);
    }
    

    public void moveToTarget(Fish food) {
        PVector target = null;
        if (food != null) target = food.getLocation();
        PVector accel = new PVector(0, 0);
        if (target != null) {
            PVector path = PVector.sub(target, getLocation());
            path.normalize();
            path.mult((float) 0.5);
            accel.add(path);
        }
        speed.add(accel);
        speed.limit(speedLimit);
        // PVector wallSteerAccel = wallPushForce().div((float) 1);
        float speedValue = speed.mag();
        // speed.add(wallSteerAccel);
        speed.normalize().mult(speedValue);
        pos.add(speed);
    }

    public void checkCollision(Dimension panelSize) {
		float margin = 150;
		Rectangle2D.Double top = new Rectangle2D.Double(-margin, -margin, panelSize.width + margin*2, margin);
		Rectangle2D.Double bottom = new Rectangle2D.Double(-margin, panelSize.height, panelSize.width + margin*2, margin);
		Rectangle2D.Double left = new Rectangle2D.Double(-margin, -margin, margin, panelSize.height + margin*2);
		Rectangle2D.Double right = new Rectangle2D.Double(panelSize.width, -margin, margin, panelSize.height + margin*2);
		
		float coef = 0.3f;
		PVector accel = new PVector();

		if (getFOV().intersects(left)) accel.add(1,0);
		else if (getFOV().intersects(right)) accel.add(-1,0);
		else if (getFOV().intersects(top)) accel.add(0,1);
		else if (getFOV().intersects(bottom)) accel.add(0,-1);
	
		float speedLimit = 2;
		accel.mult(coef * speedLimit); // Adjust the coefficient and speed limit
		speed.add(accel);
		speed.limit(speedLimit);
	
		pos.add(speed); // Update the position based on speed
	}

    // private void applyWallAvoidance(Dimension panelSize) {
    //     PVector force = new PVector();

    //     // Left wall force
    //     float distance = pos.x - size / 2 - wallCoef;
    //     if (distance < 0) {
    //         force.add(new PVector(wallCoef / (distance * distance), 0));
    //     }

    //     // Right wall force
    //     distance = panelSize.width - pos.x - size / 2 - wallCoef;
    //     if (distance < 0) {
    //         force.add(new PVector(-wallCoef / (distance * distance), 0));
    //     }

    //     // Top wall force
    //     distance = pos.y - size / 2 - wallCoef;
    //     if (distance < 0) {
    //         force.add(new PVector(0, wallCoef / (distance * distance)));
    //     }

    //     // Bottom wall force
    //     distance = panelSize.height - pos.y - size / 2 - wallCoef;
    //     if (distance < 0) {
    //         force.add(new PVector(0, -wallCoef / (distance * distance)));
    //     }

    //     speed.add(force);
    //     speed.limit(speedLimit);

    //     pos.add(speed);
    // }

	public Shape getFOV() {
		AffineTransform at = new AffineTransform();
		at.translate(pos.x, pos.y);
		at.rotate(speed.heading());
		at.scale(scalex, scaley);
		return at.createTransformedShape(fov);
	}

    private void avoidCollisions(ArrayList<Orca> orcaList) {
        for (Orca otherOrca : orcaList) {
            if (otherOrca == this) {
                continue;
            }

            float minDist = size / 2 + otherOrca.size * 2;

            if (PVector.dist(pos, otherOrca.pos) < minDist) {
                PVector pushForce = PVector.sub(pos, otherOrca.pos);
                pushForce.normalize();
                pushForce.mult(2.0f);
                speed.add(pushForce);
                speed.limit(speedLimit);

            }
        }

        pos.add(speed);
    }

    public PVector getLocation() {
        return pos;
    }
    
    public int getSize() {
        return size;
    }

    public boolean foodEaten(Fish food) {
        PVector target = food.getLocation();
        double size = (double) food.getSize();
        PVector mouthLoc = getMouthLocation();
        return PVector.dist(pos, target) < (size / 2 + size / 2) || PVector.dist(mouthLoc, target) < size / 2;
    }

    public PVector getMouthLocation() {
        PVector mouth = pos.copy();
        PVector direction = speed.copy().normalize().mult(size / 2);
        mouth.add(direction);
        return mouth;
    }

    public void setHasHit(boolean state) {
        this.hasHit = state;
        if (state) {
            hitTimer.restart();
        } else {
            hitTimer.stop();
        }
    }

    public boolean checkFoodEaten(ArrayList<Fish> foodList) {
        for (int i = 0; i < foodList.size(); i++)
            if (foodEaten(foodList.get(i))) {
                foodList.remove(i);
                return true;
            }
        return false;
    }

    public boolean collides(Orca p) {
		return (getBoundary().intersects(p.getBoundary().getBounds2D()) &&
		p.getBoundary().intersects(getBoundary().getBounds2D()));
    }

    private Shape getBoundary() {
        AffineTransform at = new AffineTransform();		
		at.translate(pos.x, pos.y);
		at.rotate(speed.heading());
		at.scale(scalex, scaley);
		return at.createTransformedShape(outline);
    }

    public void moveOut(Orca p) {
        PVector direction = PVector.sub(pos, p.getPos()).normalize();
        speed.add(direction);
    }

	private PVector getPos() {
		return pos;
	}

	protected void traceBestFood(ArrayList<Fish> fList) {
		if (fList.size()>0) {
			Fish target = fList.get(0);
			double targetAttraction = this.getAttraction(target);

			for (Fish f:fList)
				if (this.getAttraction(f) > targetAttraction) {
					target = f;
					targetAttraction = this.getAttraction(target);
				}
			this.attractedBy(target);
		}
	}

    protected void attractedBy(Fish target) {
		float coef = .2f;	// coefficient of acceleration relative to maxSpeed
		PVector direction = PVector.sub(target.getPos(), pos).normalize();
		PVector acceleration = PVector.mult(direction, maxSpeed*coef);
		speed.add(acceleration);
	}

    protected double getAttraction(Fish f) {
		return f.getSize()*10/PVector.dist(pos, f.getPos());
	}
}
