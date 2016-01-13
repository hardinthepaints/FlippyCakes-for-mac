import java.util.ArrayList;
// import java.awt.Graphics2D;
// import java.awt.Shape;
// import java.awt.Color;

class Triangle extends Region3D{

	Cell[] tri;
	float[] normal = new float[]{0, 0, 0,1};
	int direction = 1;

	public Triangle( Cell[] _tri, Item _parent, int _direction ){
		super( new ArrayList<float[]>(), _parent );
		
		curr_points = new ArrayList< float[] >();
		tri = _tri;
		for (int i = 0; i < 3; i ++ ){
			curr_points.add( tri[i].rotationCenter );
		}
		
		direction = _direction;
		
	}
	
	@Override
	public void updateSelf(  float[][] transformation, float[] pov, float[] light_src ){

        //intersectionPt2D = Projector.projectPoint( intersectionPt3D, pov);
        //System.out.println(intersectionPt2D[0] + ", " + intersectionPt2D[1]);

        //System.out.println(intersectionPt2D[0]);
        //if (region2D != null) {

//         for (float[] intersection : intersections){
//             region2D.addIntersection( Projector.projectPoint( intersection, pov) );
//         }
//         intersections.clear();
        //region2D.setIntersectionPt(intersectionPt2D);
        //}

        /* step 1 : update 3D self */
        curr_points = new ArrayList<float[]>(3);
        float[] curr_point;
        float[] new_point;
		for (int i = 0; i < 3; i ++ ){
			curr_point = tri[i].rotationCenter;
			new_point = new float[4];
			for (int j = 0; j < 3; j ++ ){
				new_point[j] = curr_point[j] + direction * normal[j];
			}
			new_point[3] = 1;
			curr_points.add( new_point );
		}

        /* step 2 : update 2D self */
        region2D.color = Colorizer.getAmbient(getNormal(), light_src);
        region2D.setPath( Projector.projectFace(curr_points, pov) );

    }
    
    
	/** getNormal
	 *
	 * @return - the vector normal to the face
	 */
	 @Override
    public float[] getNormal() {
		float[] norm = super.getNormal();
		for (int i = 0; i < 3; i++){
			norm[i] *= direction;
		}
		return norm;

    }
    



}