JLFLAGS = -g

JC = javac
.SUFFIXES: .java .class

CampaignVoucherMap.class: CampaignVoucherMap.java
	$(JC) $*.java

.java.class:
	$(JC) $*.java

CLASSES = \
	CampaignVoucherMap.java \
	UserVoucherMap.java \
	AdminWindow.java \
	ArrayMap.java \
	Campaign.java \
	CampaignWindow.java \
	ErrorWindow.java \
	MainWindow.java \
	MultipleGenerationVouchers.java \
	Notification.java \
	UpdateStatus.java \
	UserWindow.java \
	ViewCampaigns.java \
	ViewVouchers.java \
	VMS.java \
	Voucher.java \
	VoucherWindow.java

build: classes

classes: $(CLASSES:.java=.class)

run: build
	java Test
clean:	
	$(RM) *.class
