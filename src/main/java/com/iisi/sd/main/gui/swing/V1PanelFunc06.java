package com.iisi.sd.main.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.robert.study.service.TableScriptGenaratorService;
import org.robert.study.utils.POIUtils;
import org.robert.study.utils.Utils;

import com.iisi.doc.process.DocWriter;
import com.iisi.rl.table.DataTable;
import com.iisigroup.config.model.LoadServerConfigXML;
import com.iisigroup.config.model.RLRegApplicationConfigModelBean;
import com.iisigroup.config.model.RLRegApplicationConfigModelBeanConverter;
import com.iisigroup.config.model.jaxb.ItemList;
import com.iisigroup.config.model.jaxb.ItemType;
import com.iisigroup.toolkits.service.TableScriptGenaratorServiceImpl;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class V1PanelFunc06 extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = -1852906988920722628L;
    private JTree tree = null;
    private LoadServerConfigXML configXML = LoadServerConfigXML.getInstance();
    final private Set<RLRegApplicationConfigModelBean> configList = new HashSet<RLRegApplicationConfigModelBean>();
    final private V1PanelFunc06ChildPanel01 aV1PanelFunc6ChildPanel1 = new V1PanelFunc06ChildPanel01();
    final private RLRegApplicationConfigModelBeanConverter aRLRegApplicationConfigModelBeanConverter = new RLRegApplicationConfigModelBeanConverter();

    private void loadMemoryData() {
	if (aV1PanelFunc6ChildPanel1.getBeanHashMap().size() > 0) {
	    // configList.clear();
	    List<RLRegApplicationConfigModelBean> collection = new ArrayList<RLRegApplicationConfigModelBean>(aV1PanelFunc6ChildPanel1.getBeanHashMap()
		    .values());
	    final Comparator<RLRegApplicationConfigModelBean> comparator = new Comparator<RLRegApplicationConfigModelBean>() {
		public int compare(RLRegApplicationConfigModelBean o1, RLRegApplicationConfigModelBean o2) {

		    return o1.getSymbolCode().compareTo(o2.getSymbolCode());
		}
	    };
	    Collections.sort(collection, comparator);
	    configList.addAll(collection);
	}

    }

    private ItemType loadConfig() {
	final ItemType config = configXML.loadOrCreateConfig();
	List<ItemList> aItemList = config.getItemList();
	if (CollectionUtils.isNotEmpty(aItemList)) {
	    try {
		List<RLRegApplicationConfigModelBean> aaaa = aRLRegApplicationConfigModelBeanConverter.restore(aItemList.get(0));
		aV1PanelFunc6ChildPanel1.getBeanHashMap().clear();
		for (RLRegApplicationConfigModelBean bean : aaaa) {
		    aV1PanelFunc6ChildPanel1.getBeanHashMap().put(bean.hashCode(), bean);
		}
		System.out.println("總共" + aaaa.size() + "筆");
		if (aaaa != null) {
		    configList.clear();
		    configList.addAll(aaaa);
		}

	    } catch (Exception e1) {
		e1.printStackTrace();
	    }
	}
	return config;
    }

    /**
     * Create the panel.
     */
    public V1PanelFunc06() {
	final ItemType config = loadConfig();

	setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC,
		FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
		FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
		FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), }, new RowSpec[] {
		FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"),
		FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
		FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"),
		FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
		FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
		FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

	JLabel newConfigShowlabel = new JLabel("1.戶籍登記申請書table schema 批次產出作業:");
	add(newConfigShowlabel, "2, 2");

	JButton addNewConfigButton = new JButton("新增設定");
	addNewConfigButton.addActionListener(new ActionListener() {

	    public void actionPerformed(ActionEvent e) {
		try {
		    Set<String> nodeStringSet = new HashSet<String>(30);
		    DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
		    for (int i = 0; i < root.getChildCount(); ++i) {
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) root.getChildAt(i);
			Object nodeInfo = childNode.getUserObject();
			if (nodeInfo instanceof String) {
			    nodeStringSet.add((String) nodeInfo);
			}
		    }

		    // 檢查是否已有新增
		    Collection<RLRegApplicationConfigModelBean> collection = aV1PanelFunc6ChildPanel1.getBeanHashMap().values();

		    for (RLRegApplicationConfigModelBean bean : collection) {
			String symbolCode = bean.getSymbolCode();
			String chineseName = bean.getChineseName();
			if (StringUtils.isNotBlank(chineseName) && StringUtils.isNotBlank(symbolCode)) {
			    String sampleName = symbolCode + " " + chineseName;
			    if (!nodeStringSet.contains(sampleName)) {
				throw new Exception("登記代碼、登記名稱有異動,請先儲存後再作新增");
			    }
			}
			if ("新設定".equalsIgnoreCase(bean.getChineseName())) {
			    throw new Exception("已有新增的設定,請先儲存後再作新增");
			}
		    }
		    DefaultMutableTreeNode book = new DefaultMutableTreeNode("新設定");
		    aV1PanelFunc6ChildPanel1.clear();

		    root.add(book);
		    System.out.println("新增設定....");
		    DefaultTreeModel aDefaultTreeModel = (DefaultTreeModel) tree.getModel();
		    aV1PanelFunc6ChildPanel1.setVisible(true);
		    RLRegApplicationConfigModelBean newBean = new RLRegApplicationConfigModelBean();
		    newBean.setChineseName("新設定");
		    newBean.setSymbolCode("請填入類別代碼");
		    // loadMemoryData();
		    aV1PanelFunc6ChildPanel1.loadData(newBean);
		    aDefaultTreeModel.reload();
		} catch (Exception e1) {
		    e1.printStackTrace();
		    handleExceptionErrors(e1);
		}
	    }
	});
	add(addNewConfigButton, "4, 2, left, top");

	JButton saveConfigButton = new JButton("儲存設定");
	saveConfigButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if (aV1PanelFunc6ChildPanel1.getBeanHashCode() != null) {
		    aV1PanelFunc6ChildPanel1.maintainModelBeans(aV1PanelFunc6ChildPanel1.getBeanHashCode());
		}
		final List<RLRegApplicationConfigModelBean> collection = new ArrayList<RLRegApplicationConfigModelBean>(aV1PanelFunc6ChildPanel1
			.getBeanHashMap().values());
		final Comparator<RLRegApplicationConfigModelBean> comparator = new Comparator<RLRegApplicationConfigModelBean>() {
		    public int compare(RLRegApplicationConfigModelBean o1, RLRegApplicationConfigModelBean o2) {
			return o1.getSymbolCode().compareTo(o2.getSymbolCode());
		    }
		};
		for (int i = 0; i < collection.size(); ++i) {
		    RLRegApplicationConfigModelBean bean = collection.get(i);
		    if (StringUtils.isBlank(bean.getSymbolCode())) {
			collection.remove(i);
			i--;
		    }
		}
		Collections.sort(collection, comparator);
		// Map<Integer,String> recordSymbolCodeMap = new HashMap<Integer,String>();
		try {
		    // for (RLRegApplicationConfigModelBean bean : collection) {
		    // if (recordSymbolCodeMap.values().contains(bean.getSymbolCode()) ) {
		    // String tmpSymbolCode = recordSymbolCodeMap.get(Integer.valueOf(bean.hashCode()));
		    // throw new Exception("不能有重複的代碼[" + bean.getSymbolCode() + "]");
		    // } else {
		    // recordSymbolCodeMap.put(Integer.valueOf(bean.hashCode()), bean.getSymbolCode());
		    // }
		    // }

		    ItemList rlRegDataBatchitem = aRLRegApplicationConfigModelBeanConverter.generateItemList(new ArrayList<RLRegApplicationConfigModelBean>(
			    collection));
		    config.getItemList().clear();

		    config.getItemList().add(rlRegDataBatchitem);
		    // config.getItemList().add(bbbb);

		    configXML.saveConfig(config);

		    System.out.println("saving..............");
		    loadConfig();

		    DefaultTreeModel aDefaultTreeModel = (DefaultTreeModel) tree.getModel();
		    DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) aDefaultTreeModel.getRoot();
		    rootNode.removeAllChildren();
		    createNodes(rootNode);
		    aDefaultTreeModel.reload();
		} catch (Exception e1) {
		    e1.printStackTrace();
		    handleExceptionErrors(e1);
		}
	    }
	});
	add(saveConfigButton, "6, 2");

	JSplitPane splitPane = new JSplitPane();
	add(splitPane, "2, 4, 13, 19, fill, fill");

	JScrollPane scrollPane = new JScrollPane(aV1PanelFunc6ChildPanel1);
	aV1PanelFunc6ChildPanel1.setVisible(false);
	splitPane.setRightComponent(scrollPane);
	DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("戶籍登記申請書批次產生設定檔");
	createNodes(rootNode);

	DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
	MyTreeModelListener aMyTreeModelListener = new MyTreeModelListener();
	treeModel.addTreeModelListener(aMyTreeModelListener);

	tree = new JTree(treeModel);
	tree.setEditable(true);
	tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	tree.setShowsRootHandles(true);

	tree.addTreeSelectionListener(new TreeSelectionListener() {
	    public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

		/* if nothing is selected */
		if (node == null)
		    return;

		/* retrieve the node that was selected */
		Object nodeInfo = node.getUserObject();
		// aV1PanelFunc6ChildPanel1.clear();
		loadMemoryData();
		/* React to the node selection. */
		System.out.println(nodeInfo);

		if (node.getParent() == null) {
		    System.out.println("無上層");
		    aV1PanelFunc6ChildPanel1.setBeanHashCode(null);
		    aV1PanelFunc6ChildPanel1.setVisible(false);
		} else {
		    inspectAlreadyNewConfigExisting();
		    loadSelectedData(nodeInfo);
		}
	    }

	});
	splitPane.setLeftComponent(new JScrollPane(tree));

	JButton execButton = new JButton("執行作業");
	execButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		final List<RLRegApplicationConfigModelBean> collection = new ArrayList<RLRegApplicationConfigModelBean>(aV1PanelFunc6ChildPanel1
			.getBeanHashMap().values());
		execOperations(collection);
	    }
	});
	add(execButton, "2, 24, left, bottom");
    }

    private void execOperations(final List<RLRegApplicationConfigModelBean> beans) {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
	String dateString = sdf.format(new Date());
	final TableScriptGenaratorService service = new TableScriptGenaratorServiceImpl();
	final List<String> messageList = new ArrayList<String>();
	for (RLRegApplicationConfigModelBean bean : beans) {
	    List<DataTable> docList = null;
	    String beanSymbolCode = bean.getSymbolCode();
	    String beanChineseName = bean.getChineseName();
	    Map<String, String> tableTypeRefLocationMap = bean.getTableTypeRefLocationMap();
	    String eachRegRLDFSFileLocation = tableTypeRefLocationMap.get(V1PanelFunc06Constant.eachRegRLDFSFileLocation);
	    String eachRegXLDFSFileLocation = tableTypeRefLocationMap.get(V1PanelFunc06Constant.eachRegXLDFSFileLocation);
	    String externalRegFileLocation = tableTypeRefLocationMap.get(V1PanelFunc06Constant.externalRegFileLocation);
	    String regCommonFileLocation = tableTypeRefLocationMap.get(V1PanelFunc06Constant.regCommonFileLocation);
	    String xldfsCommonFileLocation = tableTypeRefLocationMap.get(V1PanelFunc06Constant.xldfsCommonFileLocation);
	    String outputFileLocation = tableTypeRefLocationMap.get(V1PanelFunc06Constant.outputFileLocation);

			try {
				// 各項登記申請書獨有欄位
				final DataTable eachRegRLDFSDataTable = service.readTemplateDataTable(new File(eachRegRLDFSFileLocation));

				// 各項XLDFS系列獨有欄位
				DataTable eachRegXLDFSDataTable = service.readTemplateDataTable(new File(eachRegXLDFSFileLocation));

				// 申請書共通欄項
				DataTable regCommonDataTable = service.readTemplateDataTable(new File(regCommonFileLocation));

				// 外來申請書共通欄項
				DataTable externalRegDataTable = service.readTemplateDataTable(new File(externalRegFileLocation));

				// XLDF共通欄項
				DataTable xldfsCommonDataTable = service.readTemplateDataTable(new File(xldfsCommonFileLocation));
				docList = service.generate64TableDocFromTemplate(beanSymbolCode, beanChineseName, eachRegRLDFSDataTable, eachRegXLDFSDataTable,
						regCommonDataTable, externalRegDataTable, xldfsCommonDataTable);
				for (DataTable aDataTable : docList) {
					service.validation(aDataTable);
				}
			} catch (Exception e) {
				handleExceptionErrors(e);
			}
	    // Map<String, String> scriptInitDataMap = service.generateInitialScript(docList);
	    Map<String, String> scriptFinalDataMap = service.generateFinalScript(docList);

	    DocWriter aDocWriter = new DocWriter();
	    try {
		String finalopputPath = outputFileLocation + File.separator + beanSymbolCode + "_" + dateString;
		String message = "匯出檔案目錄為：" + finalopputPath;
		new File(finalopputPath).mkdir();

		for (DataTable aDataTable : docList) {
		    XWPFDocument doc = aDocWriter.createNewWord(aDataTable);
		    POIUtils.writePOIXMLDocumentPartOut(new File(finalopputPath + File.separator + aDataTable.getFileName()), doc);
		}
		// for (String fileName : scriptInitDataMap.keySet()) {
		// String content = scriptInitDataMap.get(fileName);
		// File aFile = new File(finalopputPath+ File.separator+ fileName);
		// Utils.outputFile(aFile, content);
		// }
		for (String fileName : scriptFinalDataMap.keySet()) {
		    String content = scriptFinalDataMap.get(fileName);
		    File aFile = new File(finalopputPath + File.separator + fileName);
		    if (content != null) {
			Utils.outputFile(aFile, content);
		    } else {
			System.out.println("file name: " + fileName);
		    }
		}
		messageList.add(message);
	    } catch (IOException e) {
		handleExceptionErrors(e);
	    }
	}
	for (String message : messageList) {
	    JOptionPane.showMessageDialog(V1PanelFunc06.this, message, "匯出結果", JOptionPane.INFORMATION_MESSAGE);
	}
    }

    private void inspectAlreadyNewConfigExisting() {
	DefaultTreeModel aDefaultTreeModel = (DefaultTreeModel) tree.getModel();
	DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) aDefaultTreeModel.getRoot();
	try {
	    for (int i = 0; i < rootNode.getChildCount(); ++i) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) rootNode.getChildAt(i);
		if (node.getUserObject() instanceof String) {
		    String nodeInfo = (String) node.getUserObject();
		    if ("新增".equalsIgnoreCase(nodeInfo.trim())) {
			throw new Exception("有新增的登記....請先進行儲存後,再進行載入其他登記.....");
		    }
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    handleExceptionErrors(e);
	}
    }

    private void loadSelectedData(final Object nodeInfo) {
	if (nodeInfo instanceof String) {
	    if (aV1PanelFunc6ChildPanel1.getBeanHashCode() != null) {
		aV1PanelFunc6ChildPanel1.maintainModelBeans(aV1PanelFunc6ChildPanel1.getBeanHashCode());
	    }
	    StringTokenizer str = new StringTokenizer((String) nodeInfo, " ");
	    List<String> result = new ArrayList<String>();
	    while (str.hasMoreElements()) {
		result.add((String) str.nextElement());
	    }
	    if (result.size() > 1) {
		String symbolCode = result.get(0);
		String chineseName = result.get(1);
		System.out.println("symbolCode: " + symbolCode);
		System.out.println("chineseName: " + chineseName);
		for (RLRegApplicationConfigModelBean bean : configList) {
		    String beanChineseName = bean.getChineseName().trim();
		    String beanSymbolCode = bean.getSymbolCode().trim();
		    if (beanChineseName.equalsIgnoreCase(chineseName) && beanSymbolCode.equalsIgnoreCase(symbolCode)) {
			aV1PanelFunc6ChildPanel1.loadData(bean);
			aV1PanelFunc6ChildPanel1.setVisible(true);

			break;
		    } else {
			aV1PanelFunc6ChildPanel1.setBeanHashCode(null);
			aV1PanelFunc6ChildPanel1.setVisible(false);
		    }
		}
	    } else if (result.size() == 1) {
		String nodeName = (String) nodeInfo;

		System.out.println("chineseName: " + nodeName);
		for (RLRegApplicationConfigModelBean bean : configList) {
		    String beanChineseName = bean.getChineseName();
		    String beanSymbolCode = bean.getSymbolCode();
		    if (beanChineseName.equalsIgnoreCase(nodeName.trim()) || beanSymbolCode.equalsIgnoreCase(nodeName.trim())) {
			aV1PanelFunc6ChildPanel1.loadData(bean);
			aV1PanelFunc6ChildPanel1.setVisible(true);
			break;
		    } else {
			aV1PanelFunc6ChildPanel1.setBeanHashCode(null);
			aV1PanelFunc6ChildPanel1.setVisible(false);
		    }
		}

	    } else {
		aV1PanelFunc6ChildPanel1.setBeanHashCode(null);
		aV1PanelFunc6ChildPanel1.setVisible(false);
	    }
	}
	System.out.println("loading................");
    }

    private void createNodes(DefaultMutableTreeNode top) {
	DefaultMutableTreeNode category = null;
	for (RLRegApplicationConfigModelBean aRLRegApplicationConfigModelBean : configList) {
	    String symbolCode = aRLRegApplicationConfigModelBean.getSymbolCode();
	    String chineseName = aRLRegApplicationConfigModelBean.getChineseName();
	    if (StringUtils.isNotBlank(chineseName) && StringUtils.isNotBlank(symbolCode)) {
		category = new DefaultMutableTreeNode(symbolCode + " " + chineseName);

		top.add(category);
	    }

	}
    }

    class MyTreeModelListener implements TreeModelListener {
	public void treeNodesChanged(TreeModelEvent e) {
	    DefaultMutableTreeNode node;
	    node = (DefaultMutableTreeNode) (e.getTreePath().getLastPathComponent());

	    /*
	     * If the event lists children, then the changed node is the child of the node we have already gotten. Otherwise, the changed node and the specified
	     * node are the same.
	     */
	    try {
		int index = e.getChildIndices()[0];
		node = (DefaultMutableTreeNode) (node.getChildAt(index));
	    } catch (NullPointerException exc) {
	    }

	    System.out.println("The user has finished editing the node.");
	    System.out.println("New value: " + node.getUserObject());
	}

	public void treeNodesInserted(TreeModelEvent e) {
	}

	public void treeNodesRemoved(TreeModelEvent e) {
	}

	public void treeStructureChanged(TreeModelEvent e) {
	}
    }

    private void handleExceptionErrors(Exception e1) {
	StringBuffer sbf = new StringBuffer();
	sbf.append(e1.getClass()).append(" : ").append(e1.getMessage()).append("\n\r");
	e1.printStackTrace();
	Throwable cause = e1.getCause();
	handleStackTraceElement(cause, sbf);
	JOptionPane.showMessageDialog(V1PanelFunc06.this, sbf.toString(), "操作錯誤", JOptionPane.ERROR_MESSAGE);
    }

    private Throwable handleStackTraceElement(final Throwable cause, final StringBuffer sbf) {
	if (cause != null) {
	    sbf.append(cause.getClass()).append(" : ").append(cause.getMessage()).append("\n\r");
	    return handleStackTraceElement(cause.getCause(), sbf);
	} else {
	    return null;
	}
    }
}