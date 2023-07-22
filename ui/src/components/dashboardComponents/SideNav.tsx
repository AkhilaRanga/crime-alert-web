import { useNavigate, Link } from "react-router-dom";
import React, { Dispatch, SetStateAction } from "react";
import {
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Icon,
} from "@material-ui/core";
import HomeIcon from "@material-ui/icons/Home";
import HistoryIcon from "@material-ui/icons/History";
import AccountBoxIcon from "@material-ui/icons/AccountBox";
import ExitToAppIcon from "@material-ui/icons/ExitToApp";

export const sideNavTestId = "side-nav-test-id";

export enum navItems {
  FEED = "Feed",
  ACIVITY = "Activity",
  PROFILE = "Profile",
  LOGOUT = "Logout",
}

interface SideNavProps {
  setActiveNav: Dispatch<SetStateAction<navItems>>;
}

function SideNav(props: SideNavProps) {
  const { setActiveNav } = props;
  return (
    <div data-testid={sideNavTestId}>
      <List>
        <ListItem
          button
          onClick={() => setActiveNav(navItems.FEED)}
          key={navItems.FEED}
        >
          <ListItemIcon>
            <HomeIcon color="primary" />
          </ListItemIcon>
          <ListItemText primary={navItems.FEED} />
        </ListItem>
        <ListItem
          button
          onClick={() => setActiveNav(navItems.ACIVITY)}
          key={navItems.ACIVITY}
        >
          <ListItemIcon>
            <HistoryIcon color="primary" />
          </ListItemIcon>
          <ListItemText primary={navItems.ACIVITY} />
        </ListItem>
        <ListItem
          button
          onClick={() => setActiveNav(navItems.PROFILE)}
          key={navItems.PROFILE}
        >
          <ListItemIcon>
            <AccountBoxIcon color="primary" />
          </ListItemIcon>
          <ListItemText primary={navItems.PROFILE} />
        </ListItem>
        <ListItem
          button
          onClick={() => setActiveNav(navItems.LOGOUT)}
          key={navItems.LOGOUT}
        >
          <ListItemIcon>
            <ExitToAppIcon color="primary" />
          </ListItemIcon>
          <ListItemText primary={navItems.LOGOUT} />
        </ListItem>
      </List>
    </div>
  );
}

export default SideNav;
