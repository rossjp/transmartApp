<%@ page contentType="text/html;charset=ISO-8859-1"  import="org.transmart.conceptgen.*; org.transmart.conceptgen.module.*; org.transmart.conceptgen.model.*" %>
<%

//String conceptId = request.getParameter("id");
String conceptId = "758880";
String conceptTypeId = "1";
String elementSize = "98";
String conceptName = "TP53";
double fdr = 0.05;
double pValue = 0.05;

ConceptJsService cjs = new ConceptJsService();
String nodes = cjs.getConceptDirectInteractionNode(conceptId, conceptName, conceptTypeId, elementSize, pValue,fdr);
String edges = cjs.getConceptDirectInteractionEdge(conceptId, pValue,fdr);

%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html>
    <head>
        <title>Cytoscape Web example</title>
        
      	<script type="text/javascript" src="/transmart/js/cytoscape_web/json2.min.js"></script>
		<script type="text/javascript" src="/transmart/js/cytoscape_web/AC_OETags.min.js"></script>
		<script type="text/javascript" src="/transmart/js/cytoscape_web/cytoscapeweb.min.js"></script>
        
       <script type="text/javascript">
            window.onload = function() {
                // id of Cytoscape Web container div
                var div_id = "cytoscapeweb";
                
                // create a network model object
                var network_json = {
                        // you need to specify a data schema for custom attributes!
                        dataSchema: {
                            nodes: [ { name: "label", type: "string" },
                                     { name: "conceptTypeId", type: "string" },
                                     { name: "elementSize", type: "double" }
                                ]
                        },
                        // NOTE the custom attributes on nodes and edges
                        data: {
                            nodes: <%= nodes %> ,
                            edges: <%= edges %>
                        }
                };

                var visual_style = {
                        global: {
                            backgroundColor: "#ABCFD6"
                        },
                        nodes: {
                            shape: "ELLIPSE",
                            borderWidth: 2,
                            borderColor: "#ffffff",
                            size: {
                                defaultValue: 25,
                                continuousMapper: { attrName: "elementSize", minValue: 20, maxValue: 75 }
                            },
                            color: {
                                discreteMapper: {
                                    attrName: "conceptTypeId",
                                    entries: [
                                        { attrValue: 13, value: "#0B94B1" },
                                        { attrValue: 32, value: "#aad808" },
                                        { attrValue: 9, value: "#66ff99" },
                                        { attrValue: 7, value: "#3DBBB4" },
                                        { attrValue: 1, value: "#8051f7" },
                                        { attrValue: 2, value: "#72CDF9" },
                                        { attrValue: 3, value: "#386EF2" },
                                        { attrValue: 4, value: "#FF01F0" },
                                        { attrValue: 5, value: "#F5E351" },
                                        { attrValue: 12, value: "#000099" },
                                        { attrValue: 10, value: "#ff99ff" },
                                        { attrValue: 11, value: "#7D40D6" },
                                        { attrValue: 33, value: "#A681DB" },
                                        { attrValue: 6, value: "#25A832" },
                                        { attrValue: 15, value: "#BA4604" },
                                        { attrValue: 14, value: "#c7b299" },
                                        { attrValue: 16, value: "#C0C0C0" },
                                        { attrValue: 13, value: "#f68e13" }
                                        
                                    ]
                                }
                            },
                            labelHorizontalAnchor: "center"
                        },
                        edges: {
                            width: 2,
                            color: "#0B94B1"
                        }
                    };
                    
                
                // initialization options
                var options = {
                    swfPath: "/transmart/swf/CytoscapeWeb",
                    flashInstallerPath: "/transmart/swf/playerProductInstall"
                };
                
                var vis = new org.cytoscapeweb.Visualization(div_id, options);
                
                // callback when Cytoscape Web has finished drawing
                vis.ready(function() {
                
                    // add a listener for when nodes and edges are clicked
                    vis.addListener("click", "nodes", function(event) {
                        handle_click(event);
                    })
                    .addListener("click", "edges", function(event) {
                        handle_click(event);
                    });
                    
                    function handle_click(event) {
                         var target = event.target;
                         
                         clear();
                         print("event.group = " + event.group);
                         for (var i in target.data) {
                            var variable_name = i;
                            var variable_value = target.data[i];
                            print( "event.target.data." + variable_name + " = " + variable_value );
                         }
                    }
                    
                    function clear() {
                        document.getElementById("note").innerHTML = "";
                    }
                
                    function print(msg) {
                        document.getElementById("note").innerHTML += "<p>" + msg + "</p>";
                    }
                });
 
                // draw options
                var draw_options = {
                    // your data goes here
                    network: network_json,

                    visualStyle: visual_style,
                    // hide pan zoom
                    panZoomControlVisible: false 
                };
                
                vis.draw(draw_options);
            };
        </script>
        
         <style>
            /* The Cytoscape Web container must have its dimensions set. */
            html, body { height: 100%; width: 100%; padding: 0; margin: 0; }
            #cytoscapeweb { width: 100%; height: 100%; }
        </style>
    </head>
    
    <body>
        <div id="cytoscapeweb">
            Cytoscape Web will replace the contents of this div with your graph.
        </div>
    </body>
    
</html>