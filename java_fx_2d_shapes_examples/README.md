# 2D shape examples
This is a JavaFX 2D Shape Example. Any shape that can be drawn in a two-dimensional plane is called a 2D shape. JavaFX offers variety nodes to draw different types of shapes (lines, circles, rectangles, etc.). You can add shapes to a Scene Graph. All shape classes are in the javafx.scene.shape package.

A Shape has a size and a position, which are defined by their properties. For example, the width and height properties define the size of a Rectangle. The radius property defines the size of a Circle. The x and y properties define the position of the upper-left corner of a Rectangle, the centerX and centerY properties define the center of a circle, etc.

Shapes are not resized by their parents during layout. The size of a shape changes only when its size-related properties are changed. Shapes have an interior and a stroke. The properties for defining the interior and stroke of a shape are declared in the Shape class.

### 5 Drawing polygons
An instance of the Polygon class represents a polygon node. The class does not define any public properties. It lets you draw a polygon using an array of (x, y) coordinates defining the vertices of the polygon. Using the Polygon class, you can draw any type of geometric shape that is created using connected lines (triangles, pentagons, hexagons, parallelograms, etc.).

The Polygon class contains two constructors.

    Polygon()
    Polygon(double… points)

The no-args constructor creates an empty polygon. You need add the (x, y) coordinates of the vertices of the shape. The Polygon will draw a line from the first vertex to the second vertex, from the second to the third, and so on. Finally, the shape is closed by drawing a line from the last vertex to the first vertex.

The Polygon class stores the coordinates of the vertices in an ObservableList<Double>. You can get the reference of the observable list using the getPoints() method. Notice that it stores the coordinates in a list of Double, which is simply a number. It is your job to pass the numbers in pairs, so they can be used as (x, y) coordinates of vertices. If you pass an odd number of numbers, no shape is created.

The following snippet of code creates two triangles—one passes the coordinates of the vertices in the constructor and another adds them to the observable list later. Both triangles are geometrically the same.
1
2
3
4
5
	
// Create an empty triangle and add vertices later
Polygon triangle1 = new Polygon();
triangle1.getPoints().addAll(50.0, 0.0,0.0, 100.0,100.0, 100.0);
// Create a triangle with vertices
Polygon triangle2 = new Polygon(50.0, 0.0,0.0, 100.0,100.0, 100.0);

### 6 Drawing polyLines
A Polyline is similar to a Polygon, except that it does not draw a line between the last and first points. That is, a polyline is an open polygon. However, the fill color is used to fill the entire shape as if the shape was closed.

An instance of the Polyline class represents a polyline node. The class does not define any public properties. It lets you draw a polyline using an array of (x, y) coordinates defining the vertices of the polyline. Using the Polyline class, you can draw any type of geometric shape that is created using connected lines (triangles, pentagons, hexagons, parallelograms, etc.).

The Polyline class contains two constructors.

    Polyline()
    Polyline(double… points)

The no-args constructor creates an empty polyline. You need to add (x, y) coordinates of the vertices of the shape. The polygon will draw a line from the first vertex to the second vertex, from the second to the third, and so on. Unlike a Polygon, the shape is not closed automatically. If you want to close the shape, you need to add the coordinates of the first vertex as the last pair of numbers. If you want to add coordinates of vertices later, add them to the ObservableList<Double> returned by the getPoints() method of the Polyline class.

The following snippet of code creates two triangles with the same geometrical properties using different methods. Notice that the first and the last pairs of numbers are the same in order to close the triangle.
1
2
3
4
	
Polyline triangle = new Polyline();
triangle.getPoints().addAll(50.0, 0.0,  0.0, 50.0,100.0, 50.0, 50.0, 0.0);
triangle.setFill(Color.WHITE);
triangle.setStroke(Color.BLUE);

### 7 Drawing Arcs
An instance of the Arc class represents a sector of an ellipse. The class uses seven properties to define the ellipse.

    centerX
    centerY
    radiusX
    radiusY
    startAngle
    length
    type

The first four properties define an ellipse. The last three properties define a sector of the ellipse that is the Arc node. The startAngle property specifies the start angle of the section in degrees measured counterclockwise from the positive x-axis. It defines the beginning of the arc. The length is an angle in degrees measured counterclockwise from the start angle to define the end of the sector. If the length property is set to 360, the Arc is a full ellipse.

The type property specifies the way the Arc is closed. It is one of the constants, OPEN, CHORD, and ROUND, defined in the ArcType enum.

    The ArcType.OPEN does not close the arc.
    The ArcType.CHORD closes the arc by joining the starting and ending points by a straight line.
    The ArcType.ROUND closes the arc by joining the starting and ending point to the center of the ellipse.

The Arc class contains two constructors:

    Arc()
    Arc(double centerX, double centerY, double radiusX, double radiusY, double startAngle, double length)

The following code snippet creates an ligthgray Arc:
1
2
3
	
// Create an open arc with a fill
Arc arc1 = new Arc(0, 0, 50, 100, 0, 90);
arc1.setFill(Color.LIGHTGRAY);

### 8 Drawing quadratic curves
Bezier curves are used in computer graphics to draw smooth curves. An instance of the QuadCurve class represents a quadratic Bezier curve segment intersecting two specified points using a specified Bezier control point. The QuadCurve class contains six properties to specify the three points.

    startX
    startY
    controlX
    controlY
    endX
    endY

The QuadCurve class contains two constructors.

    QuadCurve()
    QuadCurve(double startX, double startY, double controlX, double controlY, double endX, double endY)

The following code snippet creates a QuadCurve:
1
2
3
4
	
// Create the QuadCurves
QuadCurve quadcurce1 = new QuadCurve(0, 100, 20, 0, 150, 100);
quadcurce1.setFill(Color.TRANSPARENT);
quadcurce1.setStroke(Color.BLACK);

### 9 Drawing Cubic Curves
An instance of the CubicCurve class represents a cubic Bezier curve segment intersecting two specified points using two specified Bezier control points. The CubicCurve class contains eight properties to specify the four points.

    startX
    startY
    controlX1
    controlY1
    controlX2
    controlY2
    endX
    endY

The CubicCurve class contains two constructors.

    CubicCurve()
    CubicCurve(double startX, double startY, double controlX1, double controlY1, double controlX2, double controlY2, double endX, double endY)

The following snippet of code creates a transparent CubicCurve:
1
2
3
4
	
// Create the CubicCurves
CubicCurve cubiccurve1 = new CubicCurve(0, 50, 20, 0, 50, 80, 50, 0);
cubiccurve1.setFill(Color.TRANSPARENT);
cubiccurve1.setStroke(Color.BLACK);

### Source
[2d shape examples](https://examples.javacodegeeks.com/desktop-java/javafx/javafx-2d-shape-example/)
[Svg addition](https://edencoding.com/svg-javafx/)