package crc;

import java.util.zip.*;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;

import org.xml.sax.InputSource;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.nio.*;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;
public class Crc
{
    public static int decode(String input)
    {
    	if(input.charAt(0)=='D')
    		return -1;
        long target = Long.parseLong(input,16);
        for(int i=0; i<10000000;i++)
        {
        CRC32 crc = new CRC32(); 
        String str = ""+i;
        crc.update(str.getBytes());
        if(crc.getValue()==target)
        {
            return i;
        }
        }
        return -1;
    }
public static byte[] unCompress(byte[] data) {  
byte[] b = null;  
try {  
    ByteArrayInputStream bis = new ByteArrayInputStream(data);  
    GZIPInputStream gzip = new GZIPInputStream(bis);  
    byte[] buf = new byte[data.length];  
    int num = -1;  
    ByteArrayOutputStream baos = new ByteArrayOutputStream();  
  
    while ((num = gzip.read(buf, 0, buf.length)) != -1) {//惆渣憩婓涴read源楊爵ㄛ羶梑善淏腔賦旰睫  
        baos.write(buf, 0, num);  
    }  
    baos.flush();  
    b = baos.toByteArray();  
    baos.close();  
    gzip.close();  
     bis.close();  
} catch (Exception ex) {  
    ex.printStackTrace();  
}  
return b;  
}
	public static byte[] readBytes(InputStream is) throws IOException
	{
		
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();

				int nRead;
				byte[] data = new byte[16384];

				while ((nRead = is.read(data, 0, data.length)) != -1) {
				  buffer.write(data, 0, nRead);
				}

				buffer.flush();

				return buffer.toByteArray();
	}
	static HttpURLConnection connection = null;
    public static String fetchString(String urlStr, String method, 
                String inputData,boolean gzip) throws IOException, DataFormatException
    {
    	//System.out.println(urlStr);
        URL url = null;
        url = new URL(urlStr);
        // create the connection
        if( connection != null)
        {
        	try
        	{
        		InputStream is = connection.getInputStream();
        		int ret = 0;
        		while ((ret = is.read()) > 0) {
        		}
        		is.close();

        	}
        	catch(Exception e){}
        	try
        	{
        		InputStream is = connection.getErrorStream();
        		int ret = 0;
        		while ((ret = is.read()) > 0) {
        		}
        		is.close();

        	}
        	catch(Exception e){}
        }
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy3.bdo.com.hk", 8080));  
        //connection = (HttpURLConnection) url.openConnection(proxy);
        connection = (HttpURLConnection) url.openConnection();
        // set the connection timeout
        connection.setConnectTimeout(20000);
        connection.setReadTimeout(20000);
        // check if any data are to be outputted to the connection
        boolean doOutput = (inputData != null && inputData.length() > 0);
        if(doOutput)
            connection.setDoOutput(true);
        //connection.setRequestProperty("Content-Type",
        //        "application/x-www-form-urlencoded");
        //connection.setRequestProperty("Accept-Language", "zh-cn,zh;q=0.5");  
        //        connection.setRequestProperty("Pragma", "no-cache");  
        //        connection.setRequestProperty("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");  
        connection.setRequestProperty("Accept-Encoding", "gzip,deflate");  
        try {
            connection.setRequestMethod(method);
            connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
            	return "failed"+connection.getResponseCode();
            if(doOutput)
            {

                // write the data to the connection
                OutputStream os = connection.getOutputStream();
                os.write(inputData.getBytes());
                os.flush();
                os.close();
            }
            if(gzip)
            {
                //System.out.println(connection.getContentEncoding());  
            	if(!connection.getContentEncoding().equals("gzip"))
            	{
            	byte [] bytes = readBytes(connection.getInputStream());
                Inflater ifl = new Inflater(true);   //mainly generate the extraction
                //df.setLevel(Deflater.BEST_COMPRESSION);
                ifl.setInput(bytes);
         
                ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length);
                byte[] buff = new byte[1024000];
                while(!ifl.finished())
                {
                    int count = ifl.inflate(buff);
                    if(count <=0)break;
                    baos.write(buff, 0, count);
                }
                baos.close();
                byte[] output = baos.toByteArray();
         
                //System.out.println("Original: "+bytes.length);
                //System.out.println("Extracted: "+output.length);
                //System.out.println("Data:");
                //System.out.println(new String(output));
                return new String(output,"utf-8");
            	}
            	
                
                InputStream in = null;
                try {
                    if(connection.getContentEncoding().equals("gzip"))
                        in = new GZIPInputStream(connection.getInputStream());  
                    else

                        in = new InflaterInputStream(connection.getInputStream(),
                              new Inflater(true));
                } catch(FileNotFoundException e) {
                    System.out.println("cannot access");
                    //System.exit(1);
                } 
            /*
                byte[] len=new byte[8];  
            InputStream dis=connection.getInputStream();
            dis.read(len);//杅擂寞寀軞酗僅  
            int length = Integer.parseInt(new String(len).trim());  
            byte[] bCode=new byte[4];  
            dis.read(bCode);//杅擂寞寀鎢  
            String code=new String(bCode);  
            length = length-8;  
            byte[] body = new byte[length];  
            int num = 0;  
            while(num<length) {//恀枙憩堤婓涴爵ㄛ埻懂岆迡dis.read(body, 0, length);  
                num += dis.read(body,num,length - num);  
            }  
            byte[] bContent = unCompress(body);//賤揤  
            return new String(bContent);  
                */
                
            Scanner sc = new Scanner(in, "utf-8");
            String res = "";
            while(sc.hasNext())
                res += sc.nextLine()+"\n";
            sc.close();
            in.close();
            return res;
                /*
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));  
                String res = "";
                String line = "";  
                while((line = reader.readLine()) != null) {  
                    System.out.println(line);
                    res += line+"\n";
                }  
                in.close();
                return res;
                */
            }
            else
            {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(),"UTF-8"));  
            String line = null;  
            String res = "";
            while ((line = reader.readLine()) != null) {  
                res += line;
            }  
            return res;
            }
            // retrieve the data from the connection
            /*
            Scanner sc = new Scanner(connection.getInputStream(), "gb2312");
            String res = "";
            while(sc.hasNext())
                res += sc.nextLine();

            return res;
            */
        } catch (ProtocolException e) {

            e.printStackTrace();
            return null;
        }
    }
    static int getNext(String src, String sub, int start)
    {
        for(int i= start; i<src.length(); i++)
        {
            try
            {
                if(src.substring(i, i+sub.length()).equals(sub))
                    return i;
            }catch(Exception e)
            {
                break;
            }
        }
        return -1;
        
    }
    static String trimHead(String src, String sub)
    {
        int ind = getNext(src, sub, 0);
        if(ind == -1)
            return null;
        return src.substring(ind+sub.length());
    }
    static String trimTail(String src, String sub)
    {
        int ind = getNext(src, sub, 0);
        if(ind == -1)
            return null;
        return src.substring(0, ind);
    }
    public static String getName(int id) throws Exception
    {
        if(id < 0) return "unknown"; 
        String url = "http://member.bilibili.tv/space?uid="+id;
        String src = fetchString(url, "GET", "",true);
        //System.out.println(src);
        String name = trimTail(trimHead(src, "<div class=\"spname\">"),"</div>");
        System.out.println(""+id+" "+name);
        return name;
    }
    public static String getVid(String url)throws Exception
    {
        String src = fetchString(url, "GET","",true);
        String cid;
        try
        {
        	cid = trimTail(trimHead(src, "cid="),"&");
        }catch (Exception e)
        {
        	try
        	{
        		cid  = trimTail(trimHead(src, "flashvars=\"cid="),"&");
        	}catch(Exception e2)
        	{
        		return null;
        	}
        }
        return cid;
    }
    public static String getId(String avUrl, TableData td) throws Exception
    {
    	if(avUrl.startsWith("av"))
    		avUrl = "http://www.bilibili.com/video/"+avUrl;
    	else if (avUrl.startsWith("www"))
    		avUrl += "http://";
    	
    	String res = "";
    	String vid;
    	if (avUrl.startsWith("http"))
    		vid = getVid(avUrl);
    	else
    		vid = avUrl;
        //String url = "http://www.bilibili.tv/dm,"+vid;
        String url = "http://comment.bilibili.com/"+vid+".xml";
        String xml = fetchString(url,"GET","",true);
        //xml = xml.replace("UTF-8", "GB2312");
        //System.out.println(xml);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        StringReader sr = new StringReader(xml);
        InputSource is = new InputSource(sr); 
        Document doc = db.parse(is);
        NodeList ps = doc.getElementsByTagName("d");
        Map<String, String> nameDict = new HashMap<String, String>();
        Map<String, Integer> idDict = new HashMap<String, Integer>();
        for(int i=0; i<ps.getLength(); i++)
        {
            try
            {
                Element p = (Element) ps.item(i);
                String attr = p.getAttributes().getNamedItem("p").getNodeValue();
                String [] splitted = attr.split(",");
                String crc = splitted[splitted.length-2];
                int id;
                String name;
                if(idDict.containsKey(crc))
                {
                    id = idDict.get(crc);
                    name = nameDict.get(crc);
                }
                else
                {
                    id = decode(crc);
                    name = getName(id);
                    idDict.put(crc, id);
                    nameDict.put(crc, name);
                }
                String cmt = p.getTextContent();
                System.out.println(""+id+"\t"+name+"\t"+cmt);
                res += crc+"\t"+id+"\t"+name+"\t"+cmt+"\n";
                if (td != null)
                	td.addRow(""+id, name, cmt);
            }catch(Exception e)
            {
            	e.printStackTrace();
                System.out.println("passed");
            }
        }
        return res;
    }
   
    
    public static String getId2(String avUrl, TableData td) throws Exception
    {
    	if(avUrl.startsWith("av"))
    		avUrl = "http://www.bilibili.com/video/"+avUrl;
    	else if (avUrl.startsWith("www"))
    		avUrl += "http://";
    	
    	String res = "";
    	String vid;
    	if (avUrl.startsWith("http"))
    		vid = getVid(avUrl);
    	else
    		vid = avUrl;
        //String url = "http://www.bilibili.tv/dm,"+vid;
        String url = "http://comment.bilibili.com/"+vid+".xml";
        String xml = fetchString(url,"GET","",true);
        //xml = xml.replace("UTF-8", "GB2312");
        //System.out.println(xml);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        StringReader sr = new StringReader(xml);
        InputSource is = new InputSource(sr); 
        Document doc = db.parse(is);
        NodeList ps = doc.getElementsByTagName("d");
        Map<String, String> nameDict = new HashMap<String, String>();
        Map<String, Integer> idDict = new HashMap<String, Integer>();
        for(int i=0; i<ps.getLength(); i++)
        {
            try
            {
                Element p = (Element) ps.item(i);
                String attr = p.getAttributes().getNamedItem("p").getNodeValue();
                String [] splitted = attr.split(",");
                String crc = splitted[splitted.length-2];
                
                String cmt = p.getTextContent();
                //System.out.println(""+id+"\t"+name+"\t"+cmt);
                res += crc+"\t"+cmt+"\n";
               // if (td != null)
                //	td.addRow(""+id, name, cmt);
            }catch(Exception e)
            {
            	e.printStackTrace();
                System.out.println("passed");
            }
        }
        return res;
    }
    public static void main(String [] args) throws Exception
    {

        //getId(args[0]);
        
        final JFrame frame = new JFrame();
        
        final JTextField input = new JTextField();
        final JTextArea  output = new JTextArea ();
        final JButton but = new JButton( "GO is not GOD");
       
        final TableData td = new TableData();
        
        but.addMouseListener(new MouseListener(){
        	class th extends Thread {
		        

		         public void run() {
		        	 //output.setText("");
		        	 td.clear();
						String url = input.getText();
						try {
							//output.setText(getId(url));
							Crc.getId(url,td);
						} catch (Exception e) {
							e.printStackTrace();
							td.addRow("","","ERROR No.114514");
						}
						but.setText("GO is not GOD");
						stat = 0;
		         }
		         
		     }
        	int stat = 0;
        	th t = null;
			@Override
			public void mouseClicked(MouseEvent arg0) {
				 
				 if (stat == 0)
				 {
					 t= new th();
					 t.start();
					 but.setText("stop");
					 stat = 1;
				 }
				 else
				 {
					 t.stop();
					 but.setText("GO is not GOD");
					 stat = 0;
				 }
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        	
        });
        JPanel content = new JPanel();//this is the panel that will be scrolled  
        JScrollPane pane = new JScrollPane(content);  
        content.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        //input.setSize(100, 20);
        content.add(input,c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        //but.setSize(100, 20);
        content.add(but,c);
        c.fill = GridBagConstraints.BOTH;
        //c.ipady = 600;      //make this component tall
        c.weightx = 0.0;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 1;
        output.setSize(500, 600);
        
        frame.getContentPane().add(pane);  
        //JScrollPane scroll = new JScrollPane(output);
        //scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        output.setEditable(false);
        //JScrollPane scroll2 = new JScrollPane(frame);
        //scroll2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        //content.add(output,c);
        
       
        final JTable table = new JTable(td);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(400-1);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 0) {
                	//if(table.getSelectedColumn()>0) return;
    	            //System.out.println(td.getUrl(table.getSelectedRow()).toString());
    	            URI uri;
    				try {
    					uri = new URL(td.getUrl(row)).toURI();
    					Desktop.getDesktop().browse(uri);
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				} 

                }
            }
        });
       
        content.add(table,c);
        frame.setSize(500, 800);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
