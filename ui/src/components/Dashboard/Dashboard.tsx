import React from "react";
import SideNav, { navItems } from "../dashboardComponents/SideNav";
import "./Dashboard.css";
import Profile from "../Profile/Profile";
import Feed from "../dashboardComponents/Feed";
import Activity from "../dashboardComponents/Activity";

export const dashboardTestId = "dashboard-test-id";

function Dashboard() {
  const [activeNav, setActiveNav] = React.useState(navItems.FEED);

  return (
    <div className="wrapper" data-testid={dashboardTestId}>
      <SideNav setActiveNav={setActiveNav} />
      {activeNav === navItems.FEED && <Feed />}
      {activeNav === navItems.ACIVITY && <Activity />}
      {activeNav === navItems.PROFILE && <Profile />}
      {activeNav === navItems.LOGOUT && <>Logout Placeholder</>}
    </div>
  );
}

export default Dashboard;
