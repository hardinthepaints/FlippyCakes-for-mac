import java.util.ArrayList;

class Glyph extends Region3D{

	/* three points which are used to calculate the normal vector */
	private ArrayList< float[] > orig_normalPoints = new ArrayList<>();
	private ArrayList< float[] > curr_normalPoints = new ArrayList<>();
	//private Item target = null;


	public Glyph(ArrayList<float[]> _orig_points, Item _parent, boolean _specularOn){
		super( _orig_points, _parent, _specularOn );
		orig_normalPoints.add( new float[]{ 0, 0, 0, 1} );
		orig_normalPoints.add( new float[]{ 1, 0, 0, 1} );
		orig_normalPoints.add( new float[]{ 1, 1, 0, 1} );
		curr_normalPoints = (ArrayList<float[]>)orig_normalPoints.clone();
	
	}
	
	@Override
	public void updateSelf( float[][] transformation, float[] pov, float[] light_src ){
		super.updateSelf( transformation, pov, light_src );
		
		/* step 1 : update normal points as well */
        curr_normalPoints = new ArrayList<float[]>(orig_normalPoints.size());
        for (float[] one_pt : orig_normalPoints){
            float[] new_pt = one_pt.clone();
            ThreeDTransform.transform(transformation, new_pt);
            curr_normalPoints.add(new_pt);
        }

	
	}

	
	/** getNormal
     *
     * @return - the vector normal to the face
     */
	@Override
    public float[] getNormal() {
        int x = 0;
        int y = 1;
        int z = 2;
        float[] point0 = curr_normalPoints.get(0);
        float[] point1 = curr_normalPoints.get(1);
        float[] point2 = curr_normalPoints.get(2);

        float[] vec_a = { point0[x] - point1[x], point0[y] - point1[y], point0[z] - point1[z], 1 };
        float[] vec_b = { point2[x] - point1[x], point2[y] - point1[y], point2[z] - point1[z], 1 };

        //the direction of the normal vector of the face
        float[] normal = VectorOps.crossProduct(vec_b, vec_a);

        if (normal[0] == 0 && normal[1] == 0 && normal[2] == 0){
            throw new IllegalArgumentException("Points must be noncollinear to calculate normal!");
        }

        return normal;

    }
	
	


}