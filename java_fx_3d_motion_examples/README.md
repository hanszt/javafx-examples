# 3D shape examples
This is a JavaFX 3D Shape example. Any shape, drawn in a three-dimensional space, having three dimensions (length, width, and depth) is known as a 3D shape.

JavaFX 8 offers two types of 3D shapes.

    Predefined shapes
    User-defined shapes

Box, Sphere, and Cylinder are three predefined 3D shapes that you can use in your JavaFX applications. You can also create any type of 3D shapes using a triangle mesh. The Box, Sphere, and Cylinder classes represent the three predefined shapes. The MeshView class represents a user-defined 3D shape in a Scene.

### Pre defined shapes
JavaFX 8 provides the following three built-in 3D geometric shapes:

    Box
    Sphere
    Cylinder

The shapes are represented by instances of the Box, Sphere, and Cylinder classes. The classes inherit from the Shape3D class, which contains three properties that are common to all types of 3D shapes:

    Material
    Draw mode
    Cull face

The properties specific to a shape type are defined in the specific class defining the Shape. All shapes are nodes. Therefore, you can apply transformations to them. You can position them at any point in the 3D space using the translateX, translateY, and translateZ transformations.
1.3 The Box

A Box is defined by the following three properties:

    width
    height
    depth

The Box class contains two constructors:

    Box()
    Box(double width, double height, double depth)

The no-args constructor creates a Box with width, height, and depth of 2.0 each. The other constructor lets you specify the dimensions of the Box. The center of the Box is located at the origin of its local coordinate system.

The following snippet of code creates a Box with width 100, height 200 and depth 100. After the creation, the Box will be transformed.
1
2
3
4
5
	
// Create a Box
Box box = new Box(100, 100, 100);
box.setTranslateX(150);
box.setTranslateY(0);
box.setTranslateZ(400);
1.4 The Sphere

A Sphere is defined by only one property named radius. The Sphere class contains three constructors:

    Sphere()
    Sphere(double radius)
    Sphere(double radius, int divisions)

The no-args constructor creates a Sphere of radius 1.0. The second constructor lets you specify the radius of the Sphere. The third constructor lets you specify the radius and divisions. A 3D sphere is made up of many divisions, which are constructed from connected triangles. The value of the number of divisions defines the resolution of the Sphere. The higher the number of divisions, the smoother the Sphere looks.

The following snippet of code creates a Sphere with radius 50. After the creation, the Spere will be transformed.
1
2
3
4
5
	
// Create a Sphere
Sphere sphere = new Sphere(50);
sphere.setTranslateX(300);
sphere.setTranslateY(-5);
sphere.setTranslateZ(400);
1.5 The Cylinder

A Cylinder is defined by two properties:

    radius
    height

The radius of the Cylinder is measured on the XZ plane. The axis of the Cylinder is measured along the y-axis. The height of the Cylinder is measured along its axis. The Cylinder class contains three constructors:

    Cylinder()
    Cylinder(double radius, double height)
    Cylinder(double radius, double height, int divisions)

The no-args constructor creates a Cylinder with a 1.0 radius and a 2.0 height. The second constructor lets you specify the radius and height properties. The third constructor lets you specify the number of divisions, which defines the resolution of the Cylinder. The higher the number of divisions, the smoother the Cylinder looks.

The following snippet of code creates a Cylinder with radius 40 and height 120. After the creation, the Cylinder will be transformed.
1
2
3
4
5
	
// Create a Cylinder
Cylinder cylinder = new Cylinder(40, 120);
cylinder.setTranslateX(500);
cylinder.setTranslateY(-25);
cylinder.setTranslateZ(600);

The details about the creation of PointLight and PerspectiveCamera will be discussed in the following chapters.

### Material properties
A Material is used for rendering the surface of shapes. You can specify the Material for the surface of 3D objects using the Material property, which is defined in the Shape3D class. The Material property is an instance of the abstract class Material. JavaFX provides the PhongMaterial class as the only concrete implementation of Material. The following properties are defined in the PhongMaterial class:

    diffuseColor
    diffuseMap
    specularColor
    specularMap
    selfIlluminationMap
    specularPower
    bumpMap

The PhongMaterial class contains three constructors:

    PhongMaterial()
    PhongMaterial(Color diffuseColor)
    PhongMaterial(Color diffuseColor, Image diffuseMap, Image specularMap, Image bumpMap, Image selfIlluminationMap)

The no-args constructor creates a PhongMaterial with the diffuse color as Color.WHITE. The other two constructors are used to create a PhongMaterial with the specified properties.

## Molecule Sample app
Use the following mouse or keyboard strokes to get the different views.

    Hold the left mouse button and drag the mouse right or left and up or down to rotate the camera view of the molecule around the axes.

    Hold the right mouse button and drag the mouse to the left to move camera view away from the water molecule model. Drag the mouse to the right to move the camera view closer to the molecule model.

    Ctrl+s is used for showing and hiding the molecule.

    Ctrl+x is used for showing and hiding the axes.

### Source
[3d shape examples](https://examples.javacodegeeks.com/desktop-java/javafx/javafx-3d-shapes-example/)
[Molecule sample app](https://docs.oracle.com/javafx/8/3d_graphics/sampleapp.htm#BABCGEAJ)
[Picking3D](https://docs.oracle.com/javase/8/javafx/graphics-tutorial/picking.htm)

