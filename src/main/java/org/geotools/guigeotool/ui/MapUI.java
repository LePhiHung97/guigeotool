package org.geotools.guigeotool.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import org.geotools.data.DataUtilities;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.tool.CursorTool;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
//import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.util.GeometricShapeFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class MapUI {
	private File sourceFile;
	private SimpleFeatureSource featureSource;
	public static MapContent map;
	public static JMapFrame mapFrame;
	private Coordinate coordPress;
	private Coordinate coordRelease;
	private List<Coordinate> lstCoordinate = new ArrayList<Coordinate>();

	public void addControls() throws IOException {
		sourceFile = JFileDataStoreChooser.showOpenFile("shp", null);
		if (sourceFile == null) {
			return;
		}
		FileDataStore store = FileDataStoreFinder.getDataStore(sourceFile);

		featureSource = store.getFeatureSource();
		System.out.println(featureSource);

		map = new MapContent();
		Style style = SLD.createSimpleStyle(featureSource.getSchema());
		Layer layer = new FeatureLayer(featureSource, style);
		layer.setTitle("Layer 1");
		map.layers().add(layer);

		mapFrame = new JMapFrame(map);
		mapFrame.enableToolBar(true);
		mapFrame.enableStatusBar(true);

		JToolBar toolBar = mapFrame.getToolBar();
		toolBar.addSeparator();
		JButton btnPoint = new JButton("Point");
		JButton btnLine = new JButton("Line");
		JButton btnPolygon = new JButton("Polygon");
		JButton btnCircle = new JButton("Circle");
		JButton btnCurveFan = new JButton("CurvesFan");
		toolBar.add(btnPoint);
		toolBar.add(btnLine);
		toolBar.add(btnPolygon);
		toolBar.add(btnCircle);
		toolBar.add(btnCurveFan);

		btnPoint.addActionListener(e -> mapFrame.getMapPane().setCursorTool(new CursorTool() {
			@Override
			public void onMouseClicked(MapMouseEvent ev) {
				drawPoint(ev);
			}
		}));
		btnLine.addActionListener(e -> mapFrame.getMapPane().setCursorTool(new CursorTool() {
			@Override
			public void onMousePressed(MapMouseEvent ev) {
				DirectPosition2D posPress = ev.getWorldPos();
				coordPress = new Coordinate(posPress.x, posPress.y);
			}

			@Override
			public void onMouseReleased(MapMouseEvent ev) {
				DirectPosition2D posRelease = ev.getWorldPos();
				coordRelease = new Coordinate(posRelease.x, posRelease.y);
				try {
					drawLine(ev);
				} catch (SchemaException e) {
					e.printStackTrace();
				}
			}

		}));
		btnPolygon.addActionListener(e -> mapFrame.getMapPane().setCursorTool(new CursorTool() {
			
			@Override
			public void onMousePressed(MapMouseEvent ev) {
				DirectPosition2D posPress = ev.getWorldPos();
				coordPress = new Coordinate(posPress.x, posPress.y);	
				System.out.println("coordPress : " + posPress.x + " : " + posPress.y);
			}

			@Override
			public void onMouseReleased(MapMouseEvent ev) {
				DirectPosition2D posRelease = ev.getWorldPos();
				coordRelease = new Coordinate(posRelease.x, posRelease.y);
				System.out.println("coordRelease : " + coordRelease.x + " : " + coordRelease.y);
				try {
					drawPolygon(ev);
				} catch (SchemaException e) {
					e.printStackTrace();
				}		
			}
		}));
		
		btnPolygon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("size : " + lstCoordinate.size());
				System.out.println("item : ");
			}
		});
		btnCircle.addActionListener(e -> mapFrame.getMapPane().setCursorTool(new CursorTool() {
			@Override
			public void onMousePressed(MapMouseEvent ev) {
				DirectPosition2D posPress = ev.getWorldPos();
				coordPress = new Coordinate(posPress.x, posPress.y);
				System.out.println("coordPress : " + posPress.x + " : " + posPress.y);
			}

			@Override
			public void onMouseReleased(MapMouseEvent ev) {
				DirectPosition2D posRelease = ev.getWorldPos();
				coordRelease = new Coordinate(posRelease.x, posRelease.y);
				System.out.println("coordRelease : " + posRelease.x + " : " + posRelease.y);
				try {
					drawCircle(ev);
				} catch (SchemaException e) {
					e.printStackTrace();
				}
			}
		}));

		
		btnCurveFan.addActionListener(e -> mapFrame.getMapPane().setCursorTool(new CursorTool() {
			
			@Override
			public void onMousePressed(MapMouseEvent ev) {
				DirectPosition2D posPress = ev.getWorldPos();
				coordPress = new Coordinate(posPress.x, posPress.y);
			}

			@Override
			public void onMouseReleased(MapMouseEvent ev) {
				DirectPosition2D posRelease = ev.getWorldPos();
				coordRelease = new Coordinate(posRelease.x, posRelease.y);
			}

			@Override
			public void onMouseClicked(MapMouseEvent ev) {
				try {
					drawCurvesFan(ev);
				} catch (SchemaException e) {
					e.printStackTrace();
				}
			}
		}));
	}

	public void drawPoint(MapMouseEvent ev) {
		DirectPosition2D pos = ev.getWorldPos();
		SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
		builder.setName("MyFeatureType");
		builder.setCRS(DefaultGeographicCRS.WGS84);
		builder.add("location", Point.class);

		SimpleFeatureType TYPE = builder.buildFeatureType();
		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);

		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
		Coordinate coord = new Coordinate(pos.x, pos.y);
		Point point = geometryFactory.createPoint(coord);
		featureBuilder.add(point);
		SimpleFeature feature = featureBuilder.buildFeature("FeaturePoint");
		List<SimpleFeature> featureCollection = new ArrayList<>();
		featureCollection.add(feature);
		Style style = SLD.createPointStyle("Circle", Color.RED, Color.RED, 0.3f, 15);
		Layer layer = new FeatureLayer(DataUtilities.collection(featureCollection), style);
		map.layers().add(layer);
	}

	public void drawLine(MapMouseEvent ev) throws SchemaException {
		Coordinate[] coords = new Coordinate[] { coordPress, coordRelease };
		/*System.out.println("---------------------------------");
		System.out.println(coordPress.x + " : " + coordPress.y);
		System.out.println(coordRelease.x + " : " + coordRelease.y);
*/
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
		org.locationtech.jts.geom.LineString line = geometryFactory.createLineString(coords);

		SimpleFeatureType TYPE = DataUtilities.createType("test", "line", "the_geom:LineString");
		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder((SimpleFeatureType) TYPE);
		featureBuilder.add(line);
		SimpleFeature feature = featureBuilder.buildFeature("LineString_Sample");

		DefaultFeatureCollection lineCollection = new DefaultFeatureCollection();
		lineCollection.add(feature);

		Style style = SLD.createLineStyle(Color.BLUE, 1);
		Layer layer = new FeatureLayer(lineCollection, style);
		map.addLayer(layer);
	}

	public void drawPolygon(MapMouseEvent ev) throws SchemaException {	
		lstCoordinate.add(coordPress);
		lstCoordinate.add(coordRelease);
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
		org.locationtech.jts.geom.LineString  line = geometryFactory.createLineString(lstCoordinate.toArray(new Coordinate[] {}));
		
		Polygon polygon = lineToPolygon(lstCoordinate, geometryFactory);
		
		SimpleFeatureType TYPE = DataUtilities.createType("test", "line", "the_geom:LineString");
		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder((SimpleFeatureType) TYPE);
		featureBuilder.add(line);
		SimpleFeature feature = featureBuilder.buildFeature("LineString_Sample");

		DefaultFeatureCollection lineCollection = new DefaultFeatureCollection();
		lineCollection.add(feature);

		Style style = SLD.createLineStyle(Color.BLUE, 1);
		Layer layer = new FeatureLayer(lineCollection, style);
		map.addLayer(layer);
	
	}
	
	public Polygon lineToPolygon(List<Coordinate> lst,GeometryFactory geometryFactory){
		LinearRing lr = geometryFactory.createLinearRing(lst.toArray(new Coordinate[] {}));
		Polygon polygon = geometryFactory.createPolygon(lr, new LinearRing[]{});
		return polygon;
	}

	public void drawCircle(MapMouseEvent ev) throws SchemaException {

		Coordinate centerPoint = new Coordinate((coordPress.x + coordRelease.x) / 2,
				(coordPress.y + coordRelease.y) / 2);
		System.out.println("center point : " + centerPoint.x + " : " + centerPoint.y);

		double radius = Math
				.sqrt(Math.pow(coordPress.x - coordRelease.x, 2) + Math.pow(coordPress.y - coordRelease.y, 2));
		System.out.println("radius : " + radius);

		GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
		shapeFactory.setNumPoints(64);
		shapeFactory.setCentre(centerPoint);
		shapeFactory.setSize(radius);
		Polygon circle = shapeFactory.createEllipse();

		SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
		builder.setName("MyFeatureType");
		builder.setCRS(DefaultGeographicCRS.WGS84);
		builder.add("location", Polygon.class);
		SimpleFeatureType TYPE = builder.buildFeatureType();
		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
		featureBuilder.add(circle);
		SimpleFeature feature = featureBuilder.buildFeature("FeaturePoint");
		List<SimpleFeature> featureCollection = new ArrayList<>();
		featureCollection.add(feature);
		Style style = SLD.createSimpleStyle(TYPE);
		Layer layer = new FeatureLayer(DataUtilities.collection(featureCollection), style);
		map.layers().add(layer);
	}

	public boolean isPointInPolygon(Polygon polygon, double longtitude, double latitute){
		Coordinate coord = new Coordinate(longtitude, latitute);
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
		Point point = geometryFactory.createPoint(coord);
		return polygon.contains(point);
	}
	public void drawCurvesFan(MapMouseEvent ev) throws SchemaException {
		
		
		
		//Create arc line
		GeometricShapeFactory geometryShapeFactory = new GeometricShapeFactory();
		//geometryShapeFactory.setNumPoints(64);
		geometryShapeFactory.setCentre(coordPress);
		geometryShapeFactory.setSize(2);
		org.locationtech.jts.geom.LineString arcline = geometryShapeFactory.createArc(45, 1);
		
		//Get list coordinate of arc line
		CoordinateSequence sequence = arcline.getCoordinateSequence();
		Coordinate arrCoordArcLine[] = new Coordinate[sequence.size() +1];
		for(int i = 0 ;i < sequence.size();  i++)
			arrCoordArcLine[i] = sequence.getCoordinate(i);
		
		//Get coord : coordMouseClick, coord Start Of arc_Line, coord End of Arc_Line 
		Coordinate coordStartArcLine = arrCoordArcLine[0];
		Coordinate coordEndArcLine = arrCoordArcLine[sequence.size()-1];
		Coordinate coordClick = new Coordinate(ev.getWorldPos().x , ev.getWorldPos().y);
		Coordinate coordMidArcLine = arrCoordArcLine[sequence.size() /2];
		
		//Create full curve fan 
		Coordinate[] arrCoordsCurve = new Coordinate[] {coordStartArcLine, coordClick,coordEndArcLine};
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
		org.locationtech.jts.geom.LineString line = geometryFactory.createLineString(arrCoordsCurve);

		//Draw Arcline
		SimpleFeatureType TYPE_ARCLINE = DataUtilities.createType("test", "line", "the_geom:LineString");
		SimpleFeatureBuilder featureBuilder_ARCLINE = new SimpleFeatureBuilder((SimpleFeatureType) TYPE_ARCLINE);
		featureBuilder_ARCLINE.add(arcline);
		SimpleFeature feature = featureBuilder_ARCLINE.buildFeature("LineString_Sample");
		DefaultFeatureCollection arcLineCollection = new DefaultFeatureCollection();
		arcLineCollection.add(feature);
		Style styleArcLine = SLD.createLineStyle(Color.GREEN, 1);
		Layer layerArcline = new FeatureLayer(arcLineCollection, styleArcLine);
		
		//Draw VLine below arcline
		SimpleFeatureType TYPE_VLINE = DataUtilities.createType("test", "line", "the_geom:LineString");
		SimpleFeatureBuilder featureBuilder_VLINE = new SimpleFeatureBuilder((SimpleFeatureType) TYPE_VLINE);
		featureBuilder_VLINE.add(line);
		SimpleFeature feature1 = featureBuilder_VLINE.buildFeature("LineString_Sample");
		DefaultFeatureCollection vLineCollection = new DefaultFeatureCollection();
		vLineCollection.add(feature1);
		Style styleVline = SLD.createLineStyle(Color.GREEN, 1);
		Layer layerVline = new FeatureLayer(vLineCollection, styleVline);
		
		//Draw Point 
		SimpleFeatureTypeBuilder builderPoint = new SimpleFeatureTypeBuilder();
		builderPoint.setName("MyFeatureType");
		builderPoint.setCRS(DefaultGeographicCRS.WGS84);
		builderPoint.add("location", Point.class);

		SimpleFeatureType TYPE_POINT = builderPoint.buildFeatureType();
		SimpleFeatureBuilder featureBuilderPoint1= new SimpleFeatureBuilder(TYPE_POINT);
		SimpleFeatureBuilder featureBuilderPoint2= new SimpleFeatureBuilder(TYPE_POINT);
		SimpleFeatureBuilder featureBuilderPoint3= new SimpleFeatureBuilder(TYPE_POINT);
		
		GeometryFactory geometryFactory2 = JTSFactoryFinder.getGeometryFactory(null);
		Point pointStartArcLine = geometryFactory2.createPoint(coordStartArcLine);
		Point pointEndArcLine = geometryFactory2.createPoint(coordEndArcLine);
		Point pointMidArcLine = geometryFactory2.createPoint(coordMidArcLine);
		featureBuilderPoint1.add(pointStartArcLine);
		featureBuilderPoint2.add(pointEndArcLine);
		featureBuilderPoint3.add(pointMidArcLine);
		
		SimpleFeature featureForPoint1 = featureBuilderPoint1.buildFeature("FeaturePoint1");
		SimpleFeature featureForPoint2 = featureBuilderPoint2.buildFeature("FeaturePoint2");
		SimpleFeature featureForPoint3 = featureBuilderPoint3.buildFeature("FeaturePoint3");
		
		List<SimpleFeature> featureCollection = new ArrayList<>();
		featureCollection.add(featureForPoint1);
		featureCollection.add(featureForPoint2);
		featureCollection.add(featureForPoint3);
		
		Style stylePoint = SLD.createPointStyle("Circle", Color.BLACK, Color.BLACK, 0.1f, 3);
		Layer layerPoint = new FeatureLayer(DataUtilities.collection(featureCollection), stylePoint);
		
		//Add layer 
		map.addLayer(layerPoint);
		map.addLayer(layerVline);
		map.addLayer(layerArcline);
		
	}
	
	public void showWindow() {
		mapFrame.setSize(1000, 800);
		mapFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mapFrame.setLocationRelativeTo(null);
		mapFrame.setVisible(true);
	}
	
	 

}
